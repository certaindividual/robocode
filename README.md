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
path to folder containing:

- package directory
    - .class file of our bot

In case of this project:  
path-to-project/target/classes

Make sure that path-to-project is not too long as it breaks the game completely. 