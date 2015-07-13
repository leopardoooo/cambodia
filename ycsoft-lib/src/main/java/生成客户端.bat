set Axis_Lib=D:\workspace\ycsoft_lib\lib
set Java_Cmd=java -Djava.ext.dirs=%Axis_Lib% 
set Output_Path=D:\workspace\ycsoft_lib\rechargecard
%Java_Cmd% org.apache.axis.wsdl.WSDL2Java -o%Output_Path% RechargeService.wsdl


