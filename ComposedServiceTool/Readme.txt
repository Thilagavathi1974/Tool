******************************
    ComposedServiceTool
******************************
1. Prerequisites
2. Details of Files used
3. Input Specification
4. Output Specification
5. How to use
******************************
        PREREQUISITES
******************************
1. Java Version 5 or above must be installed and JAVA path must have been set in environment variables
2. Python 3.x version must have been installed and path should have been set in environment variables
3. For Graph, matplotlib library must have been installed in the system along with python
******************************
    DETAILS OF FILES USED
******************************
1. Input.xls
- Input File must be in xls format

2. CSMsntm.class
- Java class used to create the data for the graph

3. ComposedServicesEmulation.py
- Pyhton Script to create visual graphs

4. jxl-2.6.jar
- JAR file used to read the data from the input.xls file

5. execute.bat
- To execute the files in a synchronized way

******************************
     INPUT SPECIFICATION
******************************
Input.xls

- Contains set of services to be composed -> 1 in each sheet of the xls file
- Contains first column as the number of replicas
- Second through the last column as next services based on the no of inputs in the tape and 
- if next service is not present for service based on the input then use '-' to mark for no service

******************************
     OUTPUT SPECIFICATION
******************************
The tool outputs 3 graphs as jpg images
- 1MSNTM.jpg
- 2MTNTM.jpg
- 3SNTMM.jpg
******************************
          HOW TO USE
******************************
1. Right click and edit the execute.bat file
2. Replace all "C:\Path\ComposedServiceTool" with the path of ComposedServiceTool folder placed in your system
3. Save and close the file
2. Replace the path of ComposedServiceTool folder in the java file and save it
4. Specify the input
4. Execute the execute.bat file to get the result