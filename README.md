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
example-run.txt       - Example run file


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
	1. load_transactions: 
		This function not only loads the transactions in-memory for faster access but at the same time finds the first frequent itemsets i.e. L1. It uses correct_first_itemset to complete the job. The itemsets are stored as a set.
	2. get_itemsets:
		This function iteratively generates itemsets which have support greater than minimum support. For the generation of candidate itemsets we have joined the previous k-1 itemsets iteratively. We keep joining elements of the previous k-1 itemsets which are above the minimum support, till we encounter an itemset of size k. This is a faster method of generating the itemsets as in python sets allow us to check if a set is sub-set of another, this leads to optimizations in the processing time.

B. Rule Generation:
	This is used to find the rules which are above the specified minimum confidence.

	For this we use the following function:
	get_rules:
		This function iterates over the frequent itemsets and without going through the transactions again, only using the support values of the itemsets finds the confidence value of each itemset. All those itemsets that exceed the minimum confidence value are formatted in the required format and added to a list for display later.



INTERESTING SAMPLE RUN:
-------------------------

<WRITE>
