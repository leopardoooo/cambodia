/**
 * UserAcct.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public interface UserAcct extends java.rmi.Remote {
    public boolean changePassword(java.lang.String userAccount, java.lang.String newPassword, java.lang.String oldPassword, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public boolean changeService(java.lang.String userAccount, java.lang.String[] serviceName, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public boolean changeStatus(java.lang.String userAccount, int status, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public boolean close(java.lang.String userAccount, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public boolean offline(java.lang.String userAccount, int offType, java.lang.String userMac, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public boolean open(service.ws.north.isam.zte.com.UserAcctInfo userAcctInfo, java.lang.String[] serviceName, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public service.ws.north.isam.zte.com.RetUserOnlineInfo queryOnlineUser(service.ws.north.isam.zte.com.QueryOnlineParam param, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public service.ws.north.isam.zte.com.RetUserFailedInfo queryUserFailedLog(service.ws.north.isam.zte.com.QueryFailedParam param, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public service.ws.north.isam.zte.com.RetUserAcctInfo queryUserInfo(java.lang.String userAccount, java.lang.String password, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
    public boolean removeBind(java.lang.String userAccount, java.lang.String[] serviceName, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException;
}
