@echo off
javac -cp E:\ComposedServiceTool\jxl-2.6.jar;. CSMsntm.java 
java -cp E:\ComposedServiceTool\jxl-2.6.jar;. CSMsntm

python ComposedServicesEmulation.py

del tempResult.txt
pause