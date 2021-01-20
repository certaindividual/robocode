# robocode

To run project in Intellij:

- add in project settings all libraries of robocode (necessary in java 11) (maybe some could be skipped):
(for latest version 15.01.2021)
bcel-6.2.jar  
codesize-1.2.jar  
picocontainer-2.14.2.jar  
robocode.battle-1.9.3.9.jar  
robocode.core-1.9.3.9.jar  
robocode.host-1.9.3.9.jar  
robocode.jar  
robocode.repository-1.9.3.9.jar  
robocode.sound-1.9.3.9.jar  
robocode.ui-1.9.3.9.jar  
robocode.ui.editor-1.9.3.9.jar  
roborumble.jar  

- add in robocode's application Options -> Preferences -> Development options -> Add 
    - path-to-project/robots  

- provide in resources/config.properties:
    - robot name and its package  
    - directory of robocode installation
  
- add "-DNOSECURITY=true -Ddebug=true" to VM settings




 