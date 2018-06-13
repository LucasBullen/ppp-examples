'''
 Copyright (c) 2018 Red Hat Inc. and others.

 This program and the accompanying materials are made
 available under the terms of the Eclipse Public License 2.0
 which is available at https://www.eclipse.org/legal/epl-2.0/

 SPDX-License-Identifier: EPL-2.0

 Contributors:
  Lucas Bullen (Red Hat Inc.) - Initial implementation
'''
import math

def UserSelectedIdFromItemList(itemList, defaultId):
    if not itemList or not 'id' in itemList[0]:
        return None
    if not defaultId:
        defaultId = itemList[0]['id']
    PrintTable(itemList)
    while(True):
        print("")
        inputtedId = raw_input('Select with id ('+defaultId+'):');
        if not inputtedId:
            return defaultId
        for item in itemList:
            if item['id'] == inputtedId:
                return inputtedId
        print('Invlaid entry, please type an id or nothing to use the default')

def PrintTable(itemList):
    longestId = 2
    longestTitle = 5
    longestDescription = 11
    for item in itemList:
        longestId = max(longestId, len(item['id']))
        longestTitle = max(longestTitle, len(item['title']))
        if 'caption' in item:
            longestDescription = max(longestDescription, len(item['caption']))
    longestId +=2
    longestTitle +=2
    longestDescription +=2

    titleString = fillStringWithWhitespace('ID',longestId, True)+'|'
    titleString += fillStringWithWhitespace('Title',longestTitle, True)+'|'
    titleString += fillStringWithWhitespace('Description',longestDescription, True)
    print(titleString)

    seperatorString = ('-' *longestId)+'+'
    seperatorString += ('-' *longestTitle)+'+'
    seperatorString += ('-' *longestDescription)+'+'
    print(seperatorString)

    for item in itemList:
        lineString = fillStringWithWhitespace(item['id'],longestId, True)+'|'
        lineString += fillStringWithWhitespace(item['title'],longestTitle, True)+'|'
        if 'caption' in item:
            lineString += fillStringWithWhitespace(item['caption'],longestDescription, True)
        print(lineString)

def fillStringWithWhitespace(string, width, center):
    length = len(string)
    if center:
        return (' ' * int(math.ceil(((width - length)/float(2)))))+string+(' ' * int(math.floor(((width - length)/float(2)))))
    else:
        return string+(' ' * (width - length))