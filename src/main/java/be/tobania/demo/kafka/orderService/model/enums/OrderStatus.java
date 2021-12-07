package be.tobania.demo.kafka.orderService.model.enums;


import lombok.Getter;

@Getter
public enum StatusEnum {

    PLACED("placed"),

    PAYED("payed"),

    SHIPPED("shipped"),

    DELIVERED("delivered");

    private String value;

private StatusEnum(String value){
    this.value = value;
}

    public static StatusEnum fromValue(String text) {
        for (StatusEnum b : values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

}