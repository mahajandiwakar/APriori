#!/usr/bin/env python

import sys
from collections import defaultdict
import decimal


class APRIORI():

    def __init__(self, minsupp, minconf):
        self.transactions=[]
        self.minsupp = float(minsupp)
        self.minconf = float(minconf)
        self.itemsets = []
        self.rules ={}

    def load_transactions(self, csv_file):
        first_itemsets = defaultdict()
        for readline in open(csv_file, 'r'):
            for line in readline.split('\r'):
                t = line.strip().split(',')
                self.transactions.append(set(t))
                for item in t:
                    item_set = frozenset([item])
                    if item_set not in first_itemsets:
                        first_itemsets[item_set]=1
                    else:
                        first_itemsets[item_set]+=1
        self.itemsets.append(first_itemsets)
        self.correct_first_itemset()

    def correct_first_itemset(self):
        num_of_transactions  = len(self.transactions)
        first_itemset = self.itemsets[0]
        for item in first_itemset.keys():
            support_value = float(first_itemset[item])/num_of_transactions
            if support_value < self.minsupp:
                del first_itemset[item]
            else:
                first_itemset[item] = support_value
        self.itemsets[0] = first_itemset


    def get_itemsets(self):
        prev_itemset = self.itemsets[0].keys()
        num_of_transactions = len(self.transactions)
        while len(prev_itemset) is not 0:
            new_set_len = len(prev_itemset[0])+1
            candidate_itemsets = defaultdict()
            for i in range(0, len(prev_itemset)):
                for j in range(i+1, len(prev_itemset)):
                    present_set = prev_itemset[i] | prev_itemset[j]
                    if len(present_set) is new_set_len:
                        count = 0
                        for itemset in prev_itemset:
                            if itemset <= present_set:
                                count += 1
                        if count == len(present_set):
                            candidate_itemsets[present_set] = 0

            if len(candidate_itemsets) is not 0:
                for t in self.transactions:
                    for itemset in candidate_itemsets:
                        if itemset <= t:
                            candidate_itemsets[itemset] += 1
                for itemset in candidate_itemsets.keys():
                    support_value = float(candidate_itemsets[itemset])/num_of_transactions
                    if support_value < self.minsupp:
                        del candidate_itemsets[itemset]
                    else:
                        candidate_itemsets[itemset] = support_value
            if len(candidate_itemsets) is not 0:
                self.itemsets.append(candidate_itemsets)
            prev_itemset = candidate_itemsets.keys()

    def get_rules(self):
        self.get_itemsets()
        for itemsets in self.itemsets[1:]:
            for itemset in itemsets.keys():
                for item in itemset:
                    left_items = itemset - frozenset([item])
                    num = self.itemsets[len(itemset) - 1][itemset]
                    num_total = self.itemsets[len(left_items) - 1][left_items]
                    conf = float(num) / num_total
                    if conf >= self.minconf:
                        line = "[" + ", ".join(list(left_items)) + "] => [" + item + "] (Conf: "+str(conf * 100) + "%, Supp: " + str ((itemsets[itemset] * 100)) + "%)"
                        self.rules[line] = conf

    def print_result(self, file):
        rules_writer = open(file, 'w')
        rules_writer.write('==Frequent itemsets (min_sup='+str(self.minsupp*100)+'%)\n')
        for itemsets in self.itemsets:
            for itemset in itemsets.keys():
                rules_writer.write(str(list(itemset)))
                rules_writer.write(', ' + str(itemsets[itemset]*100) + '%')
                rules_writer.write('\n')
        rules_writer.write('\n==High-confidence association rules (min_conf='+str(self.minconf*100)+'%)\n')
        for rules in self.rules:
            rules_writer.write(rules)
            rules_writer.write('\n')
        rules_writer.close()

if __name__ == '__main__':
    if len(sys.argv) != 4:
        print "Three inputs required : [INTEGRATED-DATASET.csv] [min_sup] [min_conf]"
        sys.exit(2)
    miner = APRIORI(sys.argv[2], sys.argv[3])
    miner.load_transactions(sys.argv[1])
    miner.get_rules()
    miner.print_result('output.txt')