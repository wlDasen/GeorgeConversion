package net.sunniwell.georgeconversion.db;

/**
 * Created by admin on 2017/11/22.
 */

public class MoneyRealRateBean {
    private String reason;
    private Result[] results;
    private int errorCode;
    public class Result {
        private String currencyF;
        private String currencyF_Name;
        private String currencyT;
        private String currencyT_Name;
        private String currencyFD;
        private String exchange;
        private String result;
        private String updateTime;

        public String getCurrencyF() {
            return currencyF;
        }

        public void setCurrencyF(String currencyF) {
            this.currencyF = currencyF;
        }

        public String getCurrencyF_Name() {
            return currencyF_Name;
        }

        public void setCurrencyF_Name(String currencyF_Name) {
            this.currencyF_Name = currencyF_Name;
        }

        public String getCurrencyT() {
            return currencyT;
        }

        public void setCurrencyT(String currencyT) {
            this.currencyT = currencyT;
        }

        public String getCurrencyT_Name() {
            return currencyT_Name;
        }

        public void setCurrencyT_Name(String currencyT_Name) {
            this.currencyT_Name = currencyT_Name;
        }

        public String getCurrencyFD() {
            return currencyFD;
        }

        public void setCurrencyFD(String currencyFD) {
            this.currencyFD = currencyFD;
        }

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            return "[Result]currencyF:" + currencyF + ",currencyF_Name:" + currencyF_Name
                    + ",currencyT:" + currencyT + ",currencyT_Name:" + currencyT_Name
                    + ",currencyFD:" + currencyFD + ",exchange:" + exchange
                    + ",result:" + result + ",updateTime:" + updateTime;
        }
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Result[] getResults() {
        return results;
    }

    public void setResults(Result[] results) {
        this.results = results;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[MoneyRealRateBean]reason:" + this.reason + ",errorCode:" + this.errorCode);
        if (this.results != null) {
            for (int i = 0; i < this.results.length; i++) {
                builder.append("\n" + this.results[i]);
            }
        }
        return builder.toString();
    }
}
