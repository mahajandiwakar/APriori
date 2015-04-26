ADVANCED DATABASES COMS E6111 - Project 3
------------------------------------------

TEAM MEMBERS:
--------------

Diwakar Mahajan - dm3084
Mahd Tauseef - 

FILES SUBMITTED:
-----------------


src/apriori.py        - A python implementation of Apriori Algorithm
run.sh                - script for running the application
data/data.csv         - Dataset in csv format
README.txt            - This File


DESCRIPTIONS:
--------------

NYC Open Data data set
------------------------

<WRITE>

Mapping the Data Set to Data.csv
---------------------------------

<WRITE>

Why this Dataset
-----------------

<WRITE>

<also write how to make this data set again>


RUNNING THE CODE:
------------------

In the directory containing run.sh, src/apriori.py use the following command:

sh run.sh <INTEGRATED_DATASET> <MINIMUM_SUPPORT> <MINIMUM_CONFIDENCE>

where:
		<INTEGRATED_DATASET> -> is the path to integrated dataset which is a csv file (e.g. xyz.csv)
		<MINIMUM_SUPPORT>    -> is the value of minimum support
		<MINIMUM_CONFIDENCE> -> is the value of minimum confidence

The program writes a 'output.txt' file listing all the frequent itemsets and rules.

Example:

sh run.sh data/data.csv 0.7 0.8

Python script can be directly called as:

python src/apriori.py <INTEGRATED_DATASET> <MINIMUM_SUPPORT> <MINIMUM_CONFIDENCE>


INTERNAL DESIGN OF THE PROJECT:
--------------------------------

There is a single Python script file 'apriori.py' that implements the APriori Algorithm. The Algorithm implementation is split into two parts:

A. Finding Large Itemsets:
	This is used to find large itemsets that are above the specified minimum support in an iterative fashion.

	For this we have the following functions:




INTERESTING SAMPLE RUN:
-------------------------

<WRITE>
