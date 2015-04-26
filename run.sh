#!/bin/bash bash

python src/apriori.py "${1}" "${2}" "${3}"

# to run the apriori algorithm
# sample usage: sh run.sh data/data.csv 0.3 0.5
# sample  run: python src/apriori.py data/data.csv 0.3 0.5