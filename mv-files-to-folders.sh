#opp_makemake -f # force the creation of the Makefile
#make all # build executable
#./channel-chooser -m -n .:../inet4/src:../inet4/examples:../inet4/tutorials:../inet4/showcases --image-path=../inet4/images -l ../inet4/src/INET -u Cmdenv -f omnetpp.ini -c PLI

#!/bin/bash

for dev in "7" "10" "12" "15"; do
        for ap in "2" "4"; do
        for rodada in {0..32}; do
                mkdir -p "${dev}-${ap}-${rodada}"
                mv "custom-${dev}-${ap}-${rodada}.txt" "${dev}-${ap}-${rodada}/custom-${dev}-${ap}-${rodada}.txt"
                mv "network-${dev}-${ap}-${rodada}.ned" "${dev}-${ap}-${rodada}/network-${dev}-${ap}-${rodada}.ned"
                mv "parameters-${dev}-${ap}-${rodada}.ini" "${dev}-${ap}-${rodada}/parameters-${dev}-${ap}-${rodada}.ini"
        done
    done
done
