package com.example.enums;

public enum WorkStatus {

        ON_LINK("на_связи"), OUT_LINK("отключен"), BANNED("заблокирован");
        private final String rusStatus;
        WorkStatus(String rus) {
            this.rusStatus=rus;
        }
        /**
         * Получение поля с русским названием статуса
         */
        public String getRus(String rus) {return rusStatus;}

}
