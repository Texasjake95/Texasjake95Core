#!/usr/bin/env bash

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


command="./gradlew setupCIWorkspace -S"
echo command
runCommand command
