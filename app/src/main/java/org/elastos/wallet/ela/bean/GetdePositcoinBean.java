package org.elastos.wallet.ela.bean;

import org.elastos.wallet.ela.rxjavahelp.BaseEntity;

public class GetdePositcoinBean  extends BaseEntity {
    /**
     * message : Query successful ^_^
     * data : {"error":null,"id":null,"jsonrpc":"2.0","result":{"available":"5000","deducted":"0"}}
     * exceptionMsg : null
     */

    private String message;
    private DataBean data;
    private Object exceptionMsg;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(Object exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public static class DataBean {
        /**
         * error : null
         * id : null
         * jsonrpc : 2.0
         * result : {"available":"5000","deducted":"0"}
         */

        private Object error;
        private Object id;
        private String jsonrpc;
        private ResultBean result;

        public Object getError() {
            return error;
        }

        public void setError(Object error) {
            this.error = error;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getJsonrpc() {
            return jsonrpc;
        }

        public void setJsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * available : 5000
             * deducted : 0
             */

            private String available;
            private String deducted;

            public String getAvailable() {
                return available;
            }

            public void setAvailable(String available) {
                this.available = available;
            }

            public String getDeducted() {
                return deducted;
            }

            public void setDeducted(String deducted) {
                this.deducted = deducted;
            }
        }
    }
}
