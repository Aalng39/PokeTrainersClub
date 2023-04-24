package vttp2023.miniproject2.server.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class PokemonList implements Serializable {
    private List<PokemonAttribute> displayList = new LinkedList<>();
    private List<PokemonAttribute> detailsList = new LinkedList<>();
    private List<PokemonAttribute> displayListByType = new LinkedList<>();
    
    public List<PokemonAttribute> getDisplayListByType() {
        return displayListByType;
    }

    public void setDisplayListByType(List<PokemonAttribute> displayListByType) {
        this.displayListByType = displayListByType;
    }

    public List<PokemonAttribute> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<PokemonAttribute> detailsList) {
        this.detailsList = detailsList;
    }

    public List<PokemonAttribute> getDisplayList() {
        return displayList;
    }

    public void setDisplayList(List<PokemonAttribute> displayList) {
        this.displayList = displayList;
    }

}
