package blockchain;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChainUtil implements Serializable {

    public static void doPayment(Map<String, String> pMap) {
        Transaction usTx = null;

    }

    // for android (user)
    public static Transaction sendFunds(PublicKey sender, PublicKey recipient, float value) {
        // 본인에게 보내려고 할 경우
        if (recipient.equals(sender)) {
            System.out.println("#Wallet#Do not send to yourself. Transaction Discarded.");
            return null;
        }
        // 보유한 금액보다 많은 양의 코인은 전송불가
        //		if (ChainUtil.measureOutputsValue(publicKey, null) < value) {
        //			System.out.println("#Wallet#Not Enough funds to send transaction. Transaction Discarded.");
        //			return null;
        //		}
        // 보유한 금액보다 많은 양의 코인은 전송불가
        // == 서버에서 inputs를 전송받는다
        List<String> inputs = ChainUtil.getInputsForTx(sender, value);
        if (inputs == null) {
            System.out.println("#Wallet#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        Transaction newTx = new Transaction(sender, recipient, value, inputs);// 인풋 리스트로 수취인에게 코인을 보낸다는 트랜잭션을 생성
        return newTx;
    }

    public static List<String> getInputsForTx(PublicKey sender, float value) {
        Map<String, String> pMap = new HashMap<>();
        Map<String, TxOutput> oMap = null;
        if (oMap != null) {
            List<String> inputs = new ArrayList<>();
            float total = 0;
            TxOutput o;
            for (Map.Entry<String, TxOutput> e : oMap.entrySet()) {
                inputs.add(e.getValue().id);
                total += e.getValue().value;
                if (total >= value) {
                    return inputs;
                }
            }
        }
        return null;
    }

    // 트랜잭션을 블록에 추가하기 전 utxos의 코인이 충분한지 검증
    public static float measureOutputsValue(PublicKey sender, List<String> outputs) {
        Map<String, String> pMap = new HashMap<>();
        Map<String, TxOutput> oMap = null;
        float total = 0;
        if (oMap != null) {
            TxOutput o;
            if (outputs != null) {
                for (String id : outputs) {
                    if ((o = oMap.get(id)) != null) {
                        total += o.value;
                    }
                }
            } else {
                // 보유중인 거래 가능한 코인 합계 검색
                for (Map.Entry<String, TxOutput> e : oMap.entrySet()) {
                    total += e.getValue().value;
                }
            }
        }
        return total;
    }

    // 블록의 트랜잭션들을 실행하기 전 utxis의 코인이 충분한지 검증
    public static float measureInputsValue(PublicKey sender, List<String> inputs) {
        Map<String, String> pMap = new HashMap<>();
        Map<String, TxOutput> iMap = null;
        float total = 0;
        if (iMap != null) {
            TxOutput o;
            if (inputs != null) {
                for (String i : inputs) {
                    if ((o = iMap.get(i)) != null) {
                        total += o.value;
                    }
                }
            } else {
                // 보유중인 거래 예정인 코인 합계 검색
                for (Map.Entry<String, TxOutput> e : iMap.entrySet()) {
                    total += e.getValue().value;
                }
            }
        }
        return total;
    }

    protected static String measureWallet(PublicKey owner) {
        float outs = ChainUtil.measureOutputsValue(owner, null);
        return outs + "(" + (outs + ChainUtil.measureInputsValue(owner, null)) + ")";
    }
}
