This folder contains the input demand for the baseline scenario.

You can download the standard inputs generated demand algorithms, from this shared folder: https://cloudstor.aarnet.edu.au/plus/s/tORm2rup7nZlkna

Alternatively, if you prefer to have your customised inputs, you can run [demand generation](https://github.com/matsim-melbourne/demand) algorithm with your desired adjustments first and use the outputs of those algorithms as the input here.

**NOTE:** The baseline scenario uses MATSim's Bicycle contrib. Therefore, make sure that you replace `bike` with `bicycle` in your inputs, i.e., plan and network, for compatibility. To do this, one easy solution is using the `sed` command in Unix. For example, for `plan.xml.gz` you can run:

```
zcat plan.xml.gz | sed 's/bike/bicycle/g' | gzip > plan_bicycle.xml.gz
```
