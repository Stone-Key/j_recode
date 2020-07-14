package jFile;

import lombok.Data;

import java.util.List;

/**
 * 
 * 方法
 */
@Data
public class Cmethoad {
    private String name;
    private List<Cparam> params;
    private Cblock sources;

}
