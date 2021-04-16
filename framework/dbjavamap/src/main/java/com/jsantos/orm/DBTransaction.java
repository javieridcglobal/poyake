package com.jsantos.orm;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author javier santos
 */


public abstract class DBTransaction {
	
	
	
	public void run() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("MTEditScreenControllerTransaction");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		TransactionStatus status = MainDb.getTxManager().getTransaction(def);
		try {
			exec();
			MainDb.getTxManager().commit(status);
		}
		catch (Throwable ex) {
			ex.printStackTrace();
			MainDb.getTxManager().rollback(status);
				throw ex;
		}
	
	}
	
	
	
	
	
	protected abstract void exec();
}
