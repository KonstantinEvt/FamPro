package com.example.enums;

public enum Status {

        ON_LINK("на_связи"), OUT_LINK("отключен"), BANNED("заблокирован");
        private final String rusStatus;
        Status(String rus) {
            this.rusStatus=rus;
        }
        /**
         * Получение поля с русским названием статуса
         */
        public String getRus(String rus) {return rusStatus;}

}
