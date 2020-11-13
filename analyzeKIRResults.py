#!/usr/bin/env python

import re
from collections import defaultdict
import csv

with open('seeds.log') as f:
    content = f.readlines()

freqTable = defaultdict(lambda: 0)

itemSet = []
locationSet = []
reexpr = ':([^{}]+?) :([^{}]+?)(,|})'
regex = re.compile(reexpr)
for i in content:
	res = regex.findall(i)
	for j in res:
		freqTable[(j[0],j[1])] = freqTable[(j[0],j[1])]+1
		if(j[0] not in itemSet):
			itemSet.append(j[0])
		if(j[1] not in locationSet):
			locationSet.append(j[1])
print(freqTable)
print(locationSet)
print(itemSet)

resultFile = open("analyzedKIRData.csv",'w')
wtr = csv.writer(resultFile,delimiter=',', lineterminator='\n')
wtr.writerow(['  ','Location'])
wtr.writerow(['Item']+locationSet)
for i in itemSet:
	print(i)
	row = [i]
	for j in locationSet:
		row.append(freqTable[(j,i)]/len(content))
	wtr.writerow(row)
