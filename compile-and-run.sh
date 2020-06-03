#!/bin/bash

for dev in "7" "10" "12" "15"; do
        for ap in "2" "4"; do
        for rodada in {0..32}; do
                cd "${dev}-${ap}-${rodada}"
                opp_makemake -f
                make all

                for config in "RANDOM" "SAME" "CUSTOM"; do
                        echo "Running ${config} config..."
                        run_command="./${dev}-${ap}-${rodada} -m -n .:../../inet4/src:../../inet4/examples:../../inet4/tutorials:../../inet4/showcases --image-path=../../inet4/images -l ../../inet4/src/INET -u Cmdenv -f parameters-${dev}-${ap}-${rodada}.ini -c ${config} > ${dev}-${ap}-${rodada}-${config}-result.txt"
                                eval $run_command
                done

                cd ..
        done
    done
done
