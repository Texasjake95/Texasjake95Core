#!/usr/bin/env bash

contains()
{
	result="false"
	if [[ "$1" =~ "$2" ]]
	then
		result="true"
	else
		result="false"
	fi
	echo "$result"
	
}

getSHA()
{
	sha=""
	while read line
		do
			if [[ $(contains "$line" "commit") == "true" ]]
			then
				sha="${line:7}"
				break
			fi
		done < commit.log
	echo "$sha"
}

# run gradle
./gradlew uploadArchives -Pfilesmaven="file:maven-repo/" -S

# change to the maven repo directory
cd ./maven-repo

# add correct origin to the repo 
git remote rm origin
git remote add origin https://Texasjake95:${GH_TOKEN}@github.com/Texasjake95/maven-repo.git

# configure repo
git config user.email "texasjake95@gmail.com"
git config user.name "Texasjake95"

# configure git push
git config push.default current

# preform commit
git add .
git commit -q -m "Travis-CI Build Push for SHA: Texasjake95/Texasjake95Core@$TRAVIS_COMMIT"

# push commit
git push -q
