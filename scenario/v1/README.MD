This folder contains the input data for the baseline scenario.

You can download the standard inputs generated using of network and demand algorithms, from this shared folder: https://osf.io/axgv8/files/osfstorage

Alternatively, if you prefer to have your customised inputs, you can run the [network generation](https://github.com/matsim-melbourne/network) and [demand generation](https://github.com/matsim-melbourne/demand) algorithms with your desired adjustments first and use the outputs of those algorithms as the input here.

**NOTE:** The baseline scenario uses MATSim's Bicycle contrib. Therefore, make sure that you replace `bike` with `bicycle` in your inputs, i.e., plan and network, for compatibility. To do this, one easy solution is using the `sed` command in Unix. For example, for `plan.xml.gz` you can run:

```
zcat plan.xml.gz | sed 's/bike/bicycle/g' | gzip > plan_bicycle.xml.gz
```
