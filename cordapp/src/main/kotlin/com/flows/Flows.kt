package com.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.MyContract
import com.template.State
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.node.services.queryBy
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
class CreateState : FlowLogic<Unit>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() {
        val notary = serviceHub.networkMapCache.notaryIdentities[0]
        val txBuilder = TransactionBuilder(notary)
                .addOutputState(State(ourIdentity), MyContract.ID)
                .addCommand(MyContract.Commands.Action(), ourIdentity.owningKey)
        txBuilder.verify(serviceHub)
        val signedTransaction = serviceHub.signInitialTransaction(txBuilder)
        subFlow(FinalityFlow(signedTransaction, listOf()))
    }
}

@InitiatingFlow
@StartableByRPC
class UpdateState : FlowLogic<Unit>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() {
        val oldState = serviceHub.vaultService.queryBy<State>().states[0]
        val txBuilder = TransactionBuilder(oldState.state.notary)
                .addInputState(oldState)
                .addOutputState(State(ourIdentity), MyContract.ID)
                .addCommand(MyContract.Commands.Action(), ourIdentity.owningKey)
        txBuilder.verify(serviceHub)
        val signedTransaction = serviceHub.signInitialTransaction(txBuilder)
        subFlow(FinalityFlow(signedTransaction, listOf()))
    }
}
