ADVANCED DATABASES COMS E6111 - Project 3
------------------------------------------

a - TEAM MEMBERS:
--------------

	Diwakar Mahajan - dm3084
	Mahd Tauseef - mt2932

b - FILES SUBMITTED:
-----------------

	src/apriori.py       - A python implementation of Apriori Algorithm
	run.sh               - script for running the application
	data/myData.csv      - Dataset in csv format
	README.md            - This File
	example-run.txt      - Example run file
	file-specs.xlsx 	 - file identifying the intitial structure of data set and which columns were eleiminated


c - DESCRIPTIONS:
--------------

	a - NYC Open Data data set
	--------------------------

	We chose 'The Stop, Question and Frisk Data' as our data set. It is in the Public Safety section of the NY Open Data website and details each incidence when an NYPD officer has approached a resident to question/frisk because of suspicion of their involvement in a crime.
	This data category can be found on the NYC Open Data website at: https://data.cityofnewyork.us/Public-Safety/The-Stop-Question-and-Frisk-Data/ftxv-d5ix
	Following the external URL provide on the web page takes you to an NYPD website which contains 'The Stop, Question and Frisk Data' for each individual year. We wanted to see the recent trends and therefore chose the Dataset for the year 2014.

	b - Mapping the Data Set to myData.csv
	--------------------------------------

	The Stop, Question and Frisk Data for the year 2014 (referred to as SQF14 henceforth) is available in a CSV format. It was a huge data set file with over 100 columns and over 45000 rows. The first step was understanding what each column referred to. This information was present on the NYPD website in another file 'The NYPD SQF file specifications'.
	Upon inspection of the dataset we realized that most of the data was extremely sparse. For example, there was a column which indicated whether an arrest was made after stopping an individual. Out of the over 45000 stops only 15% resulted in arrests. Other columns, such as whether a machine gun was retrieved upon search/frisk yielded 0 positive results. Therefore a huge number of 'attributes' were labelled N for No.
	We decided we needed to narrow down the problem to eliminate some of the 'sparseness' and in the process make it more interesting as well. The problem we decided to focus on was racial profiling which is a hot topic in US news currently (more on this in the next section).
	Therefore our next step was eliminating columns that were EXTREMELY sparse (less than 10% cells containing a positive value) AND eliminating columns that were irrelevant to our defined problem, e.g. which type of id did the suspect provide. The columns eliminated are marked red in the file-specs file provided.
	Some columns were aggregated instead of being elimated. For instance there were separate columns for what type of force was used by the police officer if any, e.g. handuffs, baton, etc. We aggregated all this in one column. Such columns are marked yellow in the file-specs.
	The next step was to exchange all the Y in the data set (Y stood for Yes) with the respective column headings. For example, all the Ys in the arrested column were changed to arrested. This was done for several columns in excel using Find and Replace.
	The next step was changing columns with a continuous spectrum of values into buckets. For example, timestop held the exact time of the stop. We changed this to a desription of the day such as morning or evening. These operations were done using sorting in Excel and then simply replacing the real values with the bucket values. Another example would be to change ages into description (old, juvenile, etc) and height into tall, short, etc.
	Some columns held code values whose real meaning was provided in the file-specs. For example in the race column a Q stood for White-Hispanic. Using Excels vlookup function we replaced any remaining columns which has code values with the code description found in the file-specs.
	The last step was eliminating all the Ns (for No) in the data set. This was done using a simple Java script which input a source file and output all attributes as is except those that held the value N.


	c - Why this Dataset
	-----------------

	On August 9 a controversy interrupted in Ferguson, Missouri when a cop shot Michael Brown, an unarmed black teenager. Soon protests erupted in the small suburb and spilled over to a many more cities over the US. Since then there have been many such cases of police shooting unarmed men of color such as the one in South Carolina earlier this month where a cop shot a man in the back and is now on trial for murder. In February a similar incident happened in New York city.
	While there have been countless such incidents over time there appears to be a recent increase in attention this issue had gotten perhaps because of the wide coverage received by the Michael Brown case. Many reports have since been published that prove racial profiling is a common practice in many Police Departments over the country.
	We chose this dataset to see whether we could find indications of racial profiling by the NYPD.


d - RUNNING THE CODE:
------------------

	In the directory containing run.sh, src/apriori.py use the following command:

	sh run.sh <INTEGRATED_DATASET> <MINIMUM_SUPPORT> <MINIMUM_CONFIDENCE>

	where:
			<INTEGRATED_DATASET> -> is the path to integrated dataset which is a csv file (e.g. xyz.csv)
			<MINIMUM_SUPPORT>    -> is the value of minimum support
			<MINIMUM_CONFIDENCE> -> is the value of minimum confidence

	The program writes a 'output.txt' file listing all the frequent itemsets and rules.

	Current value of <INTEGRATED_DATASET> is ./data/myData.csv

	Example:

	sh run.sh ./data/myData.csv 0.7 0.8

	Python script can be directly called as:

	python src/apriori.py <INTEGRATED_DATASET> <MINIMUM_SUPPORT> <MINIMUM_CONFIDENCE>


e -INTERNAL DESIGN OF THE PROJECT:
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



f- INTERESTING SAMPLE RUN:
-------------------------

	sh run.sh ./data/myData.csv 0.45 0.7

	The results show that roughly 50% of all individuals stopped and questioned by the police are black males whereas black males constitute only 12% of the cities popularion (which also include children). This supports that racial profiling does indeed take place in the NYPD. 
	One could argue that a majority of the people who eventually get arrested are African American and while this is true the Constitution prohibits targeting individual based solely on their race and thus, if a law enforcement officer “adopts a policy, employs a practice, or in a given situation takes steps to initiate an investigation of a citizen based solely upon that citizen’s race, without more, then a violation of the Equal Protection Clause has occurred.” Avery, 137 F.3d at 355.