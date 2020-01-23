package br.com.itau.journey.constant;

public enum CamundaConstants {

    TIME1("time1"),
    TIME2("time2"),
    TIME3("time3"),
    DIRECTION_A("directionA"),
    DIRECTION_B("directionB"),
    STEP("step");

    private String descricao;

    CamundaConstants(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
