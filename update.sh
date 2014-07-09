#!/bin/bash

runCommand()
{
	c=-1
	times=0
	while [ $c != 0 ]
	do
		( $1 ) 
		c=$?
		(( times++ ))
		if [ $times == 5 ]; then
			echo "5th Attempt Exiting"
			exit 1
		fi
	done
}


commandBase="gradle"

if ! foobar_loc="$(type -p "$commandBase")" || [ -z "$foobar_loc" ]; then
	commandBase="bash ./gradlew"
fi

command="$commandBase setupDecompWorkSpace"
echo "Run 1"
runCommand "$command"

echo "Run 2"
runCommand "$command"

command="$commandBase eclipse"

echo "Eclipse"
runCommand "$command"
