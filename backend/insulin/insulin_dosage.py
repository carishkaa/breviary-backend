grams = float

CARBS_RATIO_NUMERATOR = 350
INSULIN_SENSIBILITY_NUMERATOR = 110


def recommended_insulin(
        tddi: float,
        target_glycemia: float,
        current_glycemia: float,
        expected_carbohydrate_intake: grams,
        insuline_type: XXX - vlastnost pacienta, enum,
        historicke_davky: list historickych davek (puze behem poslednich 5 hodin)
) -> float:
    
    dosage_carbohydrate_intake = expected_carbohydrate_intake / (CARBS_RATIO_NUMERATOR / tddi)
    dosage_target_glycemia = (current_glycemia - target_glycemia) / (INSULIN_SENSIBILITY_NUMERATOR / tddi)
    ... sectu posledni davky davky rychleho insilinu a odectu od nasledujiciho radku.
    return max(dosage_carbohydrate_intake + dosage_target_glycemia - NOVE_VYPOCITANA_HODNOTA, 0)


if __name__ == '__main__':
    reco = recommended_insulin(30, 8, 5, 70)
    print(reco)
