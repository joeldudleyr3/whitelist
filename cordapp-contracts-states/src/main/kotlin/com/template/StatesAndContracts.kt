package com.template

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.transactions.LedgerTransaction

class MyContract : Contract {
    companion object {
        const val ID = "com.template.MyContract"
    }

    override fun verify(tx: LedgerTransaction) {
        // Verification logic goes here.
    }

    interface Commands : CommandData {
        class Action : Commands
    }
}

class State(val participant: Party) : ContractState {
    override val participants = listOf(participant)
}
