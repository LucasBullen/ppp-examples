'''
 Copyright (c) 2018 Red Hat Inc. and others.

 This program and the accompanying materials are made
 available under the terms of the Eclipse Public License 2.0
 which is available at https://www.eclipse.org/legal/epl-2.0/

 SPDX-License-Identifier: EPL-2.0

 Contributors:
  Lucas Bullen (Red Hat Inc.) - Initial implementation
'''
import json

process = None
currentMessageId = 0

def setProcess(givenProcess):
    global process
    process = givenProcess

def printErrors(response):
    if 'errorMessage' in response or ('erroneousParameters' in response and len(response['erroneousParameters']) > 0):
        print("Errors:")
        if 'errorMessage' in response:
            print(response['errorMessage'])
        return True
    return False


def sendMessage(method, parameters):
    global currentMessageId
    process.stdin.write(json.dumps({'jsonrcp':'2.0', 'id':str(currentMessageId), 'method':'projectProvisioning/' + method, 'params':parameters}) + '\n')
    currentMessageId+=1
    returnMessage = json.loads(process.stdout.readline())
    if not returnMessage:
        return returnMessage
    return returnMessage['result']


def getUserInput(prompt):
    while(True):
        userInput = raw_input(prompt)
        if userInput == 'q':
            while(True):
                confirmation = raw_input('Do you wish to quit? (y/n/i) (i=use \'q\' as input)')
                if confirmation == 'y':
                    quit()
                elif confirmation == 'i':
                    return 'q'
                elif confirmation == 'n':
                    break
                else:
                    print("Unexpected input")
        else:
            return userInput