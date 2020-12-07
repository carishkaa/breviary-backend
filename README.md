# Breviary

## Calculation approach

### Heparin

### Insulin

The function that calculates insulin bolus for patient takes as parameters:
- expected carbohydrate intake (expected_carbohydrate_intake)
- target glycemia (target_glycemia)
- current glycemia (current_glycemia)
- overall insulin daily dosage (tddi)
- rapid insuline type (Humalog, Novorapid, Apidra, Fiasp, Lyumjev)
- list of boluses in the last 6 hours (with time and insuline amount) 
```
    CARBS_RATIO_NUMERATOR = 350
    INSULIN_SENSIBILITY_NUMERATOR = 110
    dosage_carbohydrate_intake = expected_carbohydrate_intake / (CARBS_RATIO_NUMERATOR / tddi)
    dosage_target_glycemia = (current_glycemia - target_glycemia) / (INSULIN_SENSIBILITY_NUMERATOR / tddi)
    remaining_rapid_insulin = see below
    insulin_dosage = max(dosage_carbohydrate_intake + dosage_target_glycemia - current_rapid_insulin, 0)
```

- TDDI is provided by the doctor on the first day of patients arrival and then it is calculated for each day as amount of
insulin intake from previous days.
- Current glycemia should be measured.
- Target glycemia shall be provided by the doctor.
- Amount of remaining_rapid_insulin is calculated from the properties of given rapid insuline type that we take from:
https://www.ikem.cz/UserFiles/Image/1604920379L%C3%A9%C4%8Dba_inzulinem_u_diabetu_1._typu.pdf.

Specifically, based on the number of hours from previous bolus(es) we can estimate from the graphs in the pdf current 
rapid insulin level (we assume that if we have no data that current rapid insulin level is 0).