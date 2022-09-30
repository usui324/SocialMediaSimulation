package main.simulation;

import java.util.ArrayList;
import java.util.List;

public class SimulationOutputData {
    public record data(String key, double value){};
    public List<data> dataList = new ArrayList<>();

    public String[] getDataArray(){
        int length = this.dataList.size();
        String[] outputData = new String[length];

        for(OutputDataKeys key : OutputDataKeys.values()){
            int id = key.id;
            String name = key.name();

            outputData[id] = dataList.stream()
                    .filter(data -> data.key == name).findFirst()
                    .map(data -> data.value).toString();
        }
        return outputData;
    }

    public enum OutputDataKeys{
        b_all(1), b_alp(2), b_bet(3),
        l_all(4), l_pos(5), l_npo(6), l_alp(7), l_bet(8),
        q_all(9), q_alp(10), q_bet(11),
        b_rec(12), b_nre(13), l_rec(14), l_nre(15), q_rec(16), q_nre(17),
        mp_all(18), mp_alp(19), mp_bet(20), mp_rec(21), mp_nre(22),
        u_all(23), u_pos(24), u_npo(25), u_alp(26), u_bet(27),
        p_all(28), p_pos(29), p_npo(30), p_alp(31), p_bet(32),
        m_all(33), m_pos(34), m_npo(35), m_alp(36), m_bet(37),
        u_rec(38), u_nre(39), p_rec(40), p_nre(41), m_rec(42), m_nre(43),
        pt(44), rt(45),  ct(46), mt(47);

        private int id;
        private OutputDataKeys(int id){
            this.id = id;
        }
    }

    public  
}
