package test;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.set.hash.TCustomHashSet;
import gnu.trove.strategy.HashingStrategy;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import test.DataMap.IValue;
import test.DataMap.IValueHandler;

public class DataMap<NODE extends Comparable<NODE>, HANDLER extends IValueHandler<NODE, HANDLER, VALUE>, VALUE extends IValue<NODE, VALUE>> {

	public interface IValue<NODE, VALUE> extends Comparable<VALUE> {

		VALUE combineValue(VALUE value);

		VALUE copy();

		VALUE getMostAproprateValue(NODE node, VALUE value);

		boolean isValid();
	}

	public interface IValueHandler<NODE, HANDLER, VALUE extends IValue<NODE, VALUE>> extends Comparable<HANDLER> {

		VALUE determineValue();

		List<NODE> getConnectedNodes();
	}

	public enum RemovalType
	{
		SHALLOW,
		DEEP;
	}

	TCustomHashMap<NODE, HashSet<HANDLER>> map;
	protected TCustomHashMap<NODE, VALUE> valueMap;
	/**
	 * Returns a set of nodes leading to the node provided.
	 */
	TCustomHashMap<NODE, HashSet<NODE>> toEdgeMap;
	/**
	 * Returns a set of nodes leading from the node provided.
	 */
	TCustomHashMap<NODE, HashSet<NODE>> fromEdgeMap;
	TCustomHashMap<VALUE, HashSet<NODE>> reverseValue;
	TCustomHashSet<NODE> blackList;
	private TreeSet<VALUE> values = Sets.newTreeSet();
	private TCustomHashSet<NODE> avoid;
	private NODE requestedNode = null;
	private boolean debug = false;
	protected HashingStrategy<NODE> nodeHash;
	protected HashingStrategy<VALUE> valueHash;

	public DataMap()
	{
		this(new DefaultHashingStrategy<NODE>(), new DefaultHashingStrategy<VALUE>());
	}

	public DataMap(HashingStrategy<NODE> node, HashingStrategy<VALUE> value)
	{
		this.nodeHash = node;
		this.valueHash = value;
		this.map = new TCustomHashMap<NODE, HashSet<HANDLER>>(node);
		this.valueMap = new TCustomHashMap<NODE, VALUE>(node);
		this.toEdgeMap = new TCustomHashMap<NODE, HashSet<NODE>>(node);
		this.fromEdgeMap = new TCustomHashMap<NODE, HashSet<NODE>>(node);
		this.reverseValue = new TCustomHashMap<VALUE, HashSet<NODE>>(value);
		this.blackList = new TCustomHashSet<NODE>(node);
		this.avoid = new TCustomHashSet<NODE>(node);
	}

	/**
	 * Retrieve a value for the passed node.
	 *
	 * @param node
	 *            the node to get the value for
	 * @param forceRemap
	 *            force the value of the node to be remapped to a new calculated value
	 * @return a calculated or stored value.
	 */
	protected VALUE _getValue(NODE node, boolean forceRemap)
	{
		return this._getValue(this.map, node, forceRemap);
	}

	protected final VALUE _getValue(TCustomHashMap<NODE, HashSet<HANDLER>> wrapperMap, NODE node, boolean forceRemap)
	{
		if (this.isBlackListed(node))
			return null;
		if (this.debug)
			System.out.println("Resolving value for " + node);
		if (this.hasValue(node) && !forceRemap)
		{
			if (this.debug)
				System.out.println("Value for " + node + " resolved to " + this.getValueFromMap(node) + " which already existed");
			return this.getValueFromMap(node);
		}
		this.addNode(node);
		HashSet<HANDLER> wrappers = wrapperMap.get(node);
		if (wrappers == null)
		{
			if (this.debug)
				System.out.println("Could Not find wrappers for " + node);
			return null;
		}
		TreeSet<HANDLER> tempWrappers = Sets.newTreeSet(wrappers);
		VALUE best = null;
		this.avoid.add(node);
		for (HANDLER wrapper : tempWrappers)
		{
			if (this.debug)
				System.out.println("\tChecking Wrapper " + wrapper);
			boolean skip = false;
			for (NODE skipNode : this.avoid)
				for (NODE connetedNode : wrapper.getConnectedNodes())
					if (connetedNode.equals(skipNode))
						skip = true;
			if (skip && !forceRemap)
			{
				if (this.debug)
					System.out.println("\t\tSkipping " + wrapper);
				continue;
			}
			VALUE value = wrapper.determineValue();
			if (best == null)
			{
				best = value;
				continue;
			}
			best = best.getMostAproprateValue(node, value);
		}
		this.avoid.remove(node);
		if (this.debug)
			System.out.println("Value for " + node + " resolved to " + best);
		if (best == null && this.hasValue(node))
			best = this.getValueFromMap(node);
		if (best != null || forceRemap && this.hasValue(node))
			this.setValue(node, best);
		return best;
	}

	/**
	 * Add a node to the blacklist.
	 *
	 * @param node
	 *            the node to add to the blacklist
	 */
	public void addBlackList(NODE node)
	{
		this.blackList.add(node);
	}

	private void addEdge(NODE node, NODE edge)
	{
		if (this.isBlackListed(node) || this.isBlackListed(edge))
			return;
		this.addNode(node);
		this.toEdgeMap.get(node).add(edge);
		this.fromEdgeMap.get(edge).add(node);
	}

	public void addNode(NODE node)
	{
		if (this.map.containsKey(node) && this.toEdgeMap.contains(node) && this.fromEdgeMap.contains(node) || this.isBlackListed(node))
			return;
		HashSet<HANDLER> set = Sets.newHashSet();
		HashSet<NODE> edgeSet = Sets.newHashSet();
		HashSet<NODE> edgeSet2 = Sets.newHashSet();
		this.map.put(node, set);
		this.toEdgeMap.put(node, edgeSet);
		this.fromEdgeMap.put(node, edgeSet2);
	}

	protected final void addNode(TCustomHashMap<NODE, HashSet<HANDLER>> wrapperMap, NODE node)
	{
		if (wrapperMap.containsKey(node) && this.toEdgeMap.contains(node) && this.fromEdgeMap.contains(node) || this.isBlackListed(node))
			return;
		HashSet<HANDLER> set = Sets.newHashSet();
		HashSet<NODE> edgeSet = Sets.newHashSet();
		HashSet<NODE> edgeSet2 = Sets.newHashSet();
		wrapperMap.put(node, set);
		this.toEdgeMap.put(node, edgeSet);
		this.fromEdgeMap.put(node, edgeSet2);
	}

	protected void addReverseLookup(VALUE value, NODE node)
	{
		this.ensureSet(value);
		if (value != null)
		{
			this.reverseValue.get(value).add(node);
			this.values.add(value.copy());
		}
	}

	public void addWrapper(NODE node, HANDLER wrapper)
	{
		this.addWrapper(this.map, node, wrapper);
	}

	protected final void addWrapper(TCustomHashMap<NODE, HashSet<HANDLER>> wrapperMap, NODE node, HANDLER wrapper)
	{
		if (this.isBlackListed(node))
			return;
		this.addNode(node);
		this.addNode(wrapperMap, node);
		HashSet<HANDLER> wrappers = wrapperMap.get(node);
		if (wrappers.add(wrapper))
		{
			if (this.debug)
				System.out.println("Added Wrapper: " + wrapper + "to " + node);
			for (NODE edge : wrapper.getConnectedNodes())
			{
				this.addNode(edge);
				this.addEdge(node, edge);
			}
		}
	}

	/**
	 * This is used to check if value is contained in any maps that happen to be present. It
	 * defaults to the value map present in this class but allows sub classes to return their
	 * own values.
	 *
	 * @param node
	 *            the node to get the value from
	 * @return true if a value for the node present
	 */
	protected boolean containsValueInMap(NODE node)
	{
		return this.valueMap.contains(node);
	}

	/**
	 * Turn off debug mode, i.e. various print outs.
	 *
	 * @return this map
	 */
	public DataMap<NODE, HANDLER, VALUE> debugModeOff()
	{
		this.debug = false;
		return this;
	}

	/**
	 * Turn on debug mode, i.e. various print outs.
	 *
	 * @return this map
	 */
	public DataMap<NODE, HANDLER, VALUE> debugModeOn()
	{
		this.debug = true;
		return this;
	}

	/**
	 * Dump all nodes and wrappers connected to the nodes.
	 */
	public void dump()
	{
		for (Entry<NODE, HashSet<HANDLER>> entry : this.map.entrySet())
		{
			System.out.println("NODE");
			System.out.println("\t" + entry.getKey());
			System.out.println("WRAPPERS");
			for (HANDLER handler : entry.getValue())
				System.out.println("\t" + handler);
			System.out.println();
		}
	}

	/**
	 * Dump all values for this map with the nodes found in this map.
	 */
	public void dumpCompleteValueMap()
	{
		this.resolveValues(false);
		for (Entry<NODE, HashSet<HANDLER>> entry : this.map.entrySet())
			if (!this.isBlackListed(entry.getKey()))
				System.out.println(entry.getKey() + " => " + this.getValue(entry.getKey()));
	}

	/**
	 * Dump all the critical nodes through the system output.
	 */
	public void dumpCritNodes()
	{
		this.dumpCritNodes(System.out);
	}

	/**
	 * Dump all the critical nodes through the PrintStream.
	 *
	 * @param stream
	 *            the stream to print the critical nodes to
	 */
	public void dumpCritNodes(PrintStream stream)
	{
		stream.println("==========DUMPING CRIT NODEs==========");
		List<NODE> critNodes = this.getCritNodes();
		for (NODE node : critNodes)
			if (!this.isBlackListed(node))
				stream.println(node);
		stream.println("======FINSIHED DUMPING CRIT NODES======");
		stream.println("CRIT NODE SIZE: " + critNodes.size());
	}

	/**
	 * Dump all values found in the value map.
	 */
	public void dumpValueMap()
	{
		for (NODE node : this.getAllNodes())
			this.getValue(node);
		for (Entry<NODE, VALUE> entry : this.valueMap.entrySet())
			if (!this.isBlackListed(entry.getKey()))
				System.out.println(entry.getKey() + " => " + entry.getValue());
	}

	/**
	 * Ensure there is a {@link HashSet} for the value passed.
	 *
	 * @param value
	 *            the value to ensure
	 */
	private void ensureSet(VALUE value)
	{
		if (this.reverseValue.contains(value))
			return;
		this.reverseValue.put(value, new HashSet<NODE>());
	}

	/**
	 * Get all nodes found in the map.
	 *
	 * @return all nodes in the map.
	 */
	public List<NODE> getAllNodes()
	{
		ArrayList<NODE> list = Lists.newArrayList();
		for (Entry<NODE, HashSet<HANDLER>> entry : this.map.entrySet())
			list.add(entry.getKey());
		return list;
	}

	/**
	 * Get all critical nodes found in the map.
	 *
	 * @return all critical nodes.
	 */
	public List<NODE> getCritNodes()
	{
		ArrayList<NODE> critNodes = Lists.newArrayList();
		HashSet<NODE> critNodeSet = Sets.newHashSet();
		for (Entry<NODE, HashSet<HANDLER>> entry : this.map.entrySet())
		{
			this.getValue(entry.getKey());
			if (this.isBlackListed(entry.getKey()))
				continue;
			if (this.toEdgeMap.get(entry.getKey()).isEmpty())
				critNodeSet.add(entry.getKey());
		}
		for (NODE node : critNodeSet)
			critNodes.add(node);
		return critNodes;
	}

	public Set<NODE> getNodesInRange(VALUE min, VALUE max)
	{
		TreeSet<NODE> returnNodes = Sets.newTreeSet();
		if (min.compareTo(max) > 0)
		{
			VALUE temp = min;
			min = max;
			max = temp;
		}
		VALUE trueMin = this.values.floor(min);
		if (trueMin == null)
			trueMin = this.values.higher(min);
		if (trueMin == null)
			trueMin = this.values.first();
		VALUE trueMax = this.values.ceiling(max);
		if (trueMax == null)
			trueMax = this.values.lower(max);
		if (trueMax == null)
			trueMax = this.values.last();
		for (VALUE value : this.values.subSet(trueMin, true, trueMax, true))
		{
			HashSet<NODE> nodes = this.reverseValue.get(value);
			for (NODE node : nodes)
				returnNodes.add(node);
		}
		return returnNodes;
	}

	/**
	 * Retrieve a value for the passed node. <br>
	 * Same as doing getValue(node,false).
	 *
	 * @param node
	 *            the node to get the value for
	 * @return a calculated or stored value.
	 */
	public final VALUE getValue(NODE node)
	{
		return this.getValue(node, false);
	}

	/**
	 * Retrieve a value for the passed node.
	 *
	 * @param node
	 *            the node to get the value for
	 * @param forceRemap
	 *            force the value of the node to be remapped to a new calculated value
	 * @return a calculated or stored value.
	 */
	public VALUE getValue(NODE node, boolean forceRemap)
	{
		if (this.isBlackListed(node))
			return null;
		if (this.requestedNode == null)
			this.requestedNode = node;
		VALUE value = this._getValue(node, forceRemap);
		if (this.requestedNode == node)
		{
			this.avoid.clear();
			this.requestedNode = null;
		}
		return value == null ? null : value.copy();
	}

	/**
	 * This is used to obtain a value from any maps that happen to be present. It defaults to
	 * the value map present in this class but allows sub classes to return their own values.
	 *
	 * @param node
	 *            the node to get the value from
	 * @return a valid value for the node provided
	 */
	protected VALUE getValueFromMap(NODE node)
	{
		return this.valueMap.get(node);
	}

	/**
	 * This is used to check if a value is present and to validate the value is it is present.
	 *
	 * @param node
	 *            the node to get to check the value for
	 * @return true if there is a value and it is a valid value;
	 */
	public boolean hasValue(NODE node)
	{
		return this.containsValueInMap(node) && this.getValueFromMap(node) != null && this.getValueFromMap(node).isValid();
	}

	/**
	 * Is the node invalid for the map.
	 *
	 * @param node
	 *            the node to check
	 * @return true if the node is invalid.
	 */
	public boolean isBlackListed(NODE node)
	{
		return node == null || this.blackList.contains(node);
	}

	private void removeEdge(NODE edge, NODE node)
	{
		Iterator<HANDLER> iterator = this.map.get(edge).iterator();
		while (iterator.hasNext())
		{
			HANDLER handler = iterator.next();
			for (NODE connected : handler.getConnectedNodes())
				if (connected.equals(node))
				{
					if (this.debug)
						System.out.println("Removing Wrapper " + handler + " from " + edge);
					iterator.remove();
					break;
				}
		}
		boolean nodeRemoved = this.fromEdgeMap.get(edge).remove(node);
		if (this.debug && nodeRemoved)
			System.out.println("Removing edge " + node + " from " + edge);
		nodeRemoved = this.toEdgeMap.get(edge).remove(node);
		if (this.debug && nodeRemoved)
			System.out.println("Removing edge " + edge + " from " + node);
	}

	private void removeReverseLookup(VALUE value, NODE node)
	{
		if (value != null && node != null)
			this.reverseValue.get(value).remove(node);
		if (this.reverseValue.get(value).isEmpty())
		{
			this.reverseValue.remove(value);
			this.values.remove(value);
		}
	}

	public void removeValue(NODE node, boolean trueRemove, RemovalType removal)
	{
		this.removeValue(this.valueMap, node, trueRemove, removal);
	}

	public void removeValue(NODE node, RemovalType removal)
	{
		this.removeValue(node, true, removal);
	}

	protected void removeValue(TCustomHashMap<NODE, VALUE> valueMap, NODE node, boolean trueRemove, RemovalType removal)
	{
		if (this.requestedNode == null)
			this.requestedNode = node;
		if (valueMap.isEmpty())
			return;
		if (!valueMap.contains(node))
			return;
		VALUE removed = valueMap.remove(node);
		if (removed != null)
		{
			if (this.debug)
				System.out.println("Removing value for " + node);
			this.removeReverseLookup(removed.copy(), node);
		}
		else
			return;
		Iterator<NODE> edge = this.fromEdgeMap.get(node).iterator();
		while (removal == RemovalType.DEEP && edge.hasNext())
		{
			NODE to = edge.next();
			if (trueRemove && to.equals(node))
				edge.remove();
			if (this.hasValue(to) && !this.avoid.contains(node))
			{
				this.avoid.add(node);
				this.removeValue(to, false, removal);
				this.avoid.remove(node);
			}
			if (trueRemove)
				this.removeEdge(to, node);
		}
		if (trueRemove)
		{
			this.map.put(node, new HashSet<HANDLER>());
			this.toEdgeMap.put(node, new HashSet<NODE>());
			this.fromEdgeMap.put(node, new HashSet<NODE>());
		}
		if (this.requestedNode == node)
		{
			this.requestedNode = null;
			this.avoid.clear();
		}
	}

	public void resolveValues(boolean forceRemap)
	{
		this.resolveValues(this.valueMap, forceRemap);
	}

	protected void resolveValues(TCustomHashMap<NODE, VALUE> valueMap, boolean forceRemap)
	{
		TreeSet<NODE> nodes = Sets.newTreeSet();
		nodes.addAll(valueMap.keySet());
		for (NODE valueNode : nodes)
			if (this.fromEdgeMap.contains(valueNode) && this.fromEdgeMap.get(valueNode) != null)
				for (NODE node : this.fromEdgeMap.get(valueNode))
					this.getValue(node, forceRemap);
	}

	public void setValue(NODE node, VALUE value)
	{
		if (this.isBlackListed(node))
			return;
		this.addNode(node);
		if (value != null)
		{
			this.addReverseLookup(value.copy(), node);
			this.valueMap.put(node, value.copy());
		}
	}

	public DataMap<NODE, HANDLER, VALUE> toggleDebugMode()
	{
		if (this.debug)
			return this.debugModeOff();
		else
			return this.debugModeOn();
	}
}
