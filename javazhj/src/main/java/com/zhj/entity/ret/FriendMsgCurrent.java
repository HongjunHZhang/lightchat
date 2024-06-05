package com.zhj.entity.ret;

/**
 * @author 789
 */
public class FriendMsgCurrent {
        private Integer id;

        private String sendId;

        private String groupId;

        private String receiveId;

        private String sendNickName;

        private String receiveNickName;

        private String createTime;

        private String msg;

        private String countType;

        private String isValid;

        private String countPhoto;

        private String type;

        private String fileName;

        private Integer unRead;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSendId() {
            return sendId;
        }

        public void setSendId(String sendId) {
            this.sendId = sendId;
        }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getReceiveId() {
            return receiveId;
        }

        public void setReceiveId(String receiveId) {
            this.receiveId = receiveId;
        }

        public String getSendNickName() {
            return sendNickName;
        }

        public void setSendNickName(String sendNickName) {
            this.sendNickName = sendNickName;
        }

        public String getReceiveNickName() {
            return receiveNickName;
        }

        public void setReceiveNickName(String receiveNickName) {
            this.receiveNickName = receiveNickName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCountType() {
            return countType;
        }

        public void setCountType(String countType) {
            this.countType = countType;
        }

        public String getIsValid() {
            return isValid;
        }

        public void setIsValid(String isValid) {
            this.isValid = isValid;
        }

        public String getCountPhoto() {
            return countPhoto;
        }

        public void setCountPhoto(String countPhoto) {
            this.countPhoto = countPhoto;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getUnRead() {
            return unRead;
        }

        public void setUnRead(Integer unRead) {
            this.unRead = unRead;
        }
}
