'''
 Copyright (c) 2018 Red Hat Inc. and others.

 This program and the accompanying materials are made
 available under the terms of the Eclipse Public License 2.0
 which is available at https://www.eclipse.org/legal/epl-2.0/

 SPDX-License-Identifier: EPL-2.0

 Contributors:
  Lucas Bullen (Red Hat Inc.) - Initial implementation
'''
import subprocess
import webbrowser
from ListSelector import UserSelectedIdFromItemList
from Input import getUserInput, printErrors, sendMessage, setProcess

def main():
    print("**********************************************")
    print("**                                          **")
    print("** Command Line Project Provisioning Client **")
    print("**                                          **")
    print("**********************************************")
    print("")
    setProcess(subprocess.Popen(["/bin/bash", "-c", getExecutable()], stdin=subprocess.PIPE, stdout=subprocess.PIPE))
    initializeResult = initialize()
    if not initializeResult:
        print("null init return")
        return
    parameters = initializeResult["defaultProvisioningParameters"]
    while(True):
        parameters['name'] = getStringInput('name', 'Name', initializeResult["validationSupported"], parameters)
        parameters['location'] = getStringInput('location', 'Location', initializeResult["validationSupported"], parameters)
        if initializeResult['versionRequired']:
            parameters['version'] = getStringInput('version', 'Version', initializeResult["validationSupported"], parameters)
        parameters['templateSelection'] = getTemplateSelection(initializeResult['templates'], parameters['templateSelection'])
        parameters['componentVersionSelections'] = getComponentVersionSelection(initializeResult['componentVersions'], parameters['componentVersionSelections'])
        if preview(parameters):
            provisionResult = provision(parameters)
            if not provisionResult:
                continue
            print('Successful provisioning of project to ' + provisionResult['location'])
            openFiles = provisionResult['openFiles']
            if not openFiles:
                break
            print('Open suggested files?')
            for localFilePath in openFiles:
                print('    ' + localFilePath)
            if getUserInput('(y/n)') == 'y':
                for localFilePath in openFiles:
                    webbrowser.open(provisionResult['location'] + '/' + localFilePath)
            break


def getExecutable():
    while True:
        server = getUserInput('Select type of project to provision (rust, web, os):')
        if server == 'rust':
            return 'java -jar /home/lbullen/Documents/ppp_servers/rust.jar'
        elif server == 'web':
            return 'java -jar /home/lbullen/Documents/ppp_servers/web.jar'
        elif server == 'os':
            return 'java -jar /home/lbullen/Documents/ppp_servers/openshift.jar'
        else:
            print("invalid entry, try again")


def initialize():
    response = sendMessage('initalize', {'supportMarkdown': False, 'allowFileCreation': True})
    return response


def preview(parameters):
    response = sendMessage('preview', parameters)
    if printErrors(response):
        return False
    print("\nPreview: ")
    print(response['message'])
    return getUserInput('Approve of preview? (y/n)') == 'y'


def provision(parameters):
    response = sendMessage('provision', parameters)
    if printErrors(response):
        return False
    return response


def getTemplateSelection(templates, defaultTemplateSelection):
    if not templates:
        return None
    print("")
    print("Select a template")
    selectedId = UserSelectedIdFromItemList(templates, defaultTemplateSelection['id'])
    for template in templates:
        if template['id'] == selectedId:
            return {'id':selectedId, 'componentVersions':getComponentVersionSelection(template['componentVersions'], defaultTemplateSelection['componentVersions'])}
    return {'id':selectedId, 'componentVersions':[]}


def getComponentVersionSelection(componentVersions, defaultComponentVersionSelections):
    for componentVersion in componentVersions:
        print("")
        print('Select version for ' + componentVersion['title'])
        if 'caption' in componentVersion:
            print('(' + componentVersion['caption'] + ')')
        index = None
        for x in xrange(len(defaultComponentVersionSelections)):
            if defaultComponentVersionSelections[x]['id'] == componentVersion['id']:
                defaultSelection = defaultComponentVersionSelections[x]['versionId']
                index = x
                break
        if index != None:
            defaultComponentVersionSelections[index]['versionId'] = UserSelectedIdFromItemList(componentVersion['versions'], defaultSelection)
        else:
            defaultComponentVersionSelections.append({'id':componentVersion['id'], 'versionId':UserSelectedIdFromItemList(componentVersion['versions'], None)})
    return defaultComponentVersionSelections


def getStringInput(filedId, label, validationSupported, defaultParameters):
    defaultName = defaultParameters[filedId];
    prompt = label + ' (' + defaultName + '):'
    isNameValid = False
    while not isNameValid:
        name = getUserInput(prompt)
        if not name:
            name = defaultName
        if validationSupported:
            defaultParameters[filedId] = name
            isNameValid = validate(defaultParameters)
        else:
            isNameValid = True
    return name


def validate(parameters):
    response = sendMessage('validation', parameters)
    return not printErrors(response)

if __name__ == '__main__':
    main()
