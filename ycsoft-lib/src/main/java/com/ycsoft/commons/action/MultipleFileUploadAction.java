package com.ycsoft.commons.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.abstracts.AbstractAction;
import com.ycsoft.commons.helper.LoggerHelper;

/**
 * 支持多个文件上传，一般情况下需要扩展该类添加额外的属性。
 *
 * @author hh
 * @date Dec 3, 2009 4:00:45 PM
 */
public class MultipleFileUploadAction extends AbstractAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 3430367609413175777L;
	//upload file property
    private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();


    /**
     * 控制函数，需要重写该函数实现文件的保存功能
     */
    @Override
    public String execute() throws Exception {
    	LoggerHelper.debug(MultipleFileUploadAction.class,"\n\n\t\tupload file execute...");
    	if(uploads.size() == 0){
    		LoggerHelper.warn(MultipleFileUploadAction.class,"multiplefileupload.upload_file_not_found");
    	}
        return SUCCESS;
    }

    public List<File> getUpload() {
        return uploads;
    }
    public void setUpload(List<File> uploads) {
        this.uploads = uploads;
    }

    public List<String> getUploadFileName() {
        return uploadFileNames;
    }
    public void setUploadFileName(List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
    }

    public List<String> getUploadContentType() {
        return uploadContentTypes;
    }
    public void setUploadContentType(List<String> contentTypes) {
        uploadContentTypes = contentTypes;
    }

	public List<File> getUploads() {
		return uploads;
	}

	public void setUploads(List<File> uploads) {
		this.uploads = uploads;
	}

	public List<String> getUploadFileNames() {
		return uploadFileNames;
	}

	public void setUploadFileNames(List<String> uploadFileNames) {
		this.uploadFileNames = uploadFileNames;
	}

	public List<String> getUploadContentTypes() {
		return uploadContentTypes;
	}

	public void setUploadContentTypes(List<String> uploadContentTypes) {
		this.uploadContentTypes = uploadContentTypes;
	}
}