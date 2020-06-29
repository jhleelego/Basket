package blockchain;

import java.io.Serializable;
import java.security.PublicKey;

public class TxOutput implements Serializable {
	public String id = null;// 코인의 해시코드
	public PublicKey reciepient = null;// 코인 주인
	public float value = 0;// 코인 갯수
	public String parentTxId = null;// 코인이 발행된 트랜잭션의 해시코드

	public TxOutput(PublicKey reciepient, float value, String parentTxId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTxId = parentTxId;
		StringBuilder sb = new StringBuilder(HashUtil.getStringFromKey(reciepient));
		sb.append(value);
		sb.append(parentTxId);
		this.id = HashUtil.toSHA3_256(sb.toString());
	}

	// 해당 키가 이 트랜잭션 아웃풋의 주인인지 확인
	public boolean isMine(PublicKey key) {
		return (reciepient.equals(key));
	}
}
