/**
 * JCaCommand.java	2010/09/22
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * JCaCommand -> J_CA_COMMAND_invalid mapping
 */
@POJO(tn = "J_CA_COMMAND_OSD_INVALID ", sn = "SEQ_CA_TRANSNUM", pk = "transnum")
public class JCaCommandOsdInvalid extends JCaCommand implements Serializable {
}