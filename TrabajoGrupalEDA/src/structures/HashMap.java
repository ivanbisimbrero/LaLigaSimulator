/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import LaLiga.Team;

/**
 *
 * @author Antonio Gabarrús Nerin (alu142920)
 * @description ---ADD DESCRIPTION---
 */
public class HashMap {

    private int tableSize;
    private float loadFactor;
    private int numOfElems;
    private Object[] map;

    /**
     * Returns the next prime number of the number given
     *
     * @param number a positive number (or zero)
     * @return the next prime number of number or number itself if it is prime
     */
    private int nextPrime(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Prime numbers only apply to positive numbers");
        }
        for (int i = 2; i < (number / 2) + 1; ++i) { // It is not necessary cto check all the numbers, just the first half between 2 and number
            if (number % i == 0) { //The division can be performed, therefore it is not a prime number
                number++;
                i = 2;
                //We restart the cycle with the next number
            }
        }
        return number;
    }

    public HashMap(int estimatedKeys) {
        this.tableSize = nextPrime(((int) ((float) estimatedKeys / 0.8)) + 1);
        this.loadFactor = 0f;
        this.numOfElems = 0;
        this.map = new Object[tableSize];
    }

    public int hash(String clave, boolean isInsert) {
        if (isInsert && numOfElems == this.tableSize) {
            return -1; //Prevents infinite loop over cuadratic exploration
        }
        int i = 0, p;
        long d;
        //como la clave de dispersion es de tipo cadena, primero se convierte a un valor entero
        d = getLongFromString(clave);

        //aplica aritmetica modular para obtener la direcci�n base
        p = (int) (d % this.tableSize);

        //Exploración cuadrática
        while (this.map[p] != null && (!(((Team) this.map[p]).getName().equals(clave)) || (isInsert && !((Team) this.map[p]).isActive()))) {
            i++;
            p = p + i * i;
            p = p % this.tableSize; // Ensure the index is still in bouds of array
        }

        return p;
    }

    private long getLongFromString(String clave) {
        //metodo de multiplicacion para realizar la transformacion. 
        //detalles de como se obtiene, queda fuera del ambito del tema
        long d = 0;
        for (int j = 0; j < Math.min(10, clave.length()); j++) {
            d = d * 29 + (int) clave.charAt(j);
        }

        if (d < 0) {
            d = -d;
        }
        return d;
    }

    public boolean put(Team team) {
        int hash = hash(team.getName(), true);
        if (hash != -1) {
            this.map[hash] = team;
            this.loadFactor = (float) (++numOfElems / this.tableSize);
            return true;
        }
        return false;
    }

    private String[] getKeysArray() {
        String[] keysArray = new String[numOfElems];
        int i = 0;
        for (Object ob : this.map) {
            if (ob != null && ((Team)ob).isActive() ){
                keysArray[i] = ((Team) ob).getName();
                ++i;
            }
        }
        return keysArray;
    }

    public boolean containsKey(String key) {
        boolean contained = false;
        for (String k : getKeysArray()) {
            if (k.equals(key)) {
                return true;
            }
        }
        return contained;
    }

    public Team get(String key) {
        if (containsKey(key)) {
            int hash = hash(key, false);
            return (((Team) this.map[hash]).isActive()) ? (Team) this.map[hash] : null;
        }
        return null;
    }
    
    public Team remove(String key){
        Team myTeamToRemove = null;
        if(this.containsKey(key)){
            myTeamToRemove = (Team)this.map[this.hash(key, false)];
            myTeamToRemove.setActive(false);
            this.numOfElems--;
        }
        return myTeamToRemove;
    }
    
    public Team[] getValuesArray(){
        Team[] myTeamArray = new Team[this.numOfElems];
        int lastElement = -1;
        for (int i = 0; i < myTeamArray.length; i++){
            for (int j = ++lastElement; j < this.map.length; ++j){
                if (this.map[j]!= null && ((Team) this.map[j]).isActive()){
                    lastElement = j;
                    myTeamArray[i] = (Team) this.map[j];
                    break;
                }
            }
        }
        return myTeamArray;
    }
    
}
