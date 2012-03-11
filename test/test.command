#!/bin/bash

cp ../deps/craftbukkit.jar craftbukkit.jar

cp ../target/Rakamak.jar plugins/Rakamak.jar

java -Xmx512M -jar craftbukkit.jar
