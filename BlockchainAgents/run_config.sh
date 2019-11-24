#!/bin/sh
# ==================
# BLOCKCHAIN AGENTS 
# ==================
#
# Ip which is used by the machine in Local Network
instance_ip='192.168.0.10'

# Agent specifications in format
# <name>:<checkTimeInMiliseconds>,<amountToBuy>,<agentType>
# name must be unique in agents from this machine
# chechTimeInMiliseconds: 10000 is ok
# agentType: only Client or Miner
# if amountToBuy is 0 then agent (usually miner) is not going to be client
#
# Example
# configs=(
#             'client:10000,100,Client'
#             'miner:1000,200,Miner'
#         )

# shellcheck disable=SC2039
configs=(
            'client:100,Client'
            'miner:200,Miner'
        )

#------------------------------------------------

agentConst=":blockchain.agents."

agentConfigs=""
for rawConfigs in ${configs[@]}
do
    unset agentRawConfigs
    if [[ $rawConfigs == *":"* ]]
    then
        tmpArray=(${rawConfigs//:/ })
        rawConfigs=${tmpArray[0]}
        agentRawConfigs=${tmpArray[1]}
        agentRawConfigs=(${agentRawConfigs//,/ })
    fi

    agentConfigs=$agentConfigs$rawConfigs$agentConst${agentRawConfigs[1]}Agent'('${agentRawConfigs[0]},${agentRawConfigs[1]}');'
done
# echo $agentConfigs



java -cp agents.jar jade.Boot -mtp "jade.mtp.http.MessageTransportProtocol(http://$instance_ip:7778/acc)" -gui "$agentConfigs"