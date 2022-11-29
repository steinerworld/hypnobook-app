-- Tax period
INSERT INTO public.tax_period (id, geschaeftsjahr, von, bis, status)
VALUES ('a65a92da-b125-4649-9beb-918dac8feb82', 2021, '2021-01-01', '2021-12-31', 'GESCHLOSSEN');
INSERT INTO public.tax_period (id, geschaeftsjahr, von, bis, status)
VALUES ('3ff9b559-063d-46bd-89be-69aa1ed18cbf', 2022, '2022-01-01', '2022-12-31', 'AKTIV');
INSERT INTO public.tax_period (id, geschaeftsjahr, von, bis, status)
VALUES ('0ff49e81-21fa-4edb-b5ae-8f8a08f80532', 2023, '2023-01-01', '2023-12-31', 'ERSTELLT');


--Categorie
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('8e4d9561-2ae9-4b62-9d29-0ae89c93be46', 'Direkter Aufwand',
        'Z.B.: Material, Waren, Fremdleistungen. Hier erfassen Sie alle Ausgaben, die direkt für die Erledigung von Kundenaufträgen benötigt werden. Wenn Sie z.B. als Grafiker ein lizenziertes Foto für ein Kundenprojekt kaufen oder als Fotograf die Materialkosten für die Fotoentwicklung oder den Bilderrahmen.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('84b83b60-e009-4e6f-8dc1-426df49b49ba', 'AHV/IV/EO-Beiträge', '1. Säule. Ihre persönlichen AHV/IV/EO-Beiträge.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('568301b7-a1b5-4b7d-bfcf-b15f7146ec92', 'BVG/Pensionskassen-Beiträge',
        '2. Säule, nur zu 50%. Da die 2. Säule für Einzelunternehmen freiwillig ist, haben die meisten Selbständigen hier keine Ausgaben. Wenn Sie diese aber haben, dann dürfen Sie lediglich 50% der tatsächlich bezahlten Beiträge im HypnoBook angeben. Die anderen 50% bezahlen Sie als Privatperson, nicht als Unternehmer.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('f75f3aa2-e22e-4cd8-ba9a-cd0c73da17d1', 'Übrige Sozialversicherungen',
        'Unfall, Krankentaggeld. Private Versicherungen oder Krankenkasse dürfen hingegen nicht erfasst werden.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('76c5c28e-79f2-4bea-8275-1e5258cf3db4', 'Miete Praxis/Büro',
        'Wenn Sie ein Büro gemietet haben, ist die Ausgabe eindeutig. Wenn Sie aber Zuhause arbeiten, können Sie einen Teil der privaten Miete abziehen. Wie gross dieser Anteil sein darf, ist von Kanton zu Kanton unterschiedlich.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('6f7a2ab7-74d9-4d4b-9336-62f51ffa758a', 'Büromaterial, IT, Telefon, Porti',
        'Ein Hinweis speziell zur EDV, wenn Sie beispielsweise einen Laptop kaufen: Als Faustregel gilt, dass Beträge bis CHF 1000.- ganz normal als Ausgaben erfasst werden können. Grössere Ausgaben müssen abgeschrieben werden. Das heisst, über 2 oder mehr Jahre wird lediglich ein Teil des gesamten Betrages als Ausgabe erfasst.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('a12e2d37-ee42-43e1-ac5c-9b0ed534662a', 'Bankspesen', 'Die Kontogebühren Ihres Geschäftskontos, die meistens jedes Quartal anfallen.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('e164cbe4-5eb1-4dd7-89a0-33fb18da1bc6', 'Fahrzeug/Benzin', 'Ausgaben für Ihr Auto.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('ce9ef240-ce22-4a23-95af-c33884487757', 'Reparatur und Unterhalt',
        'Für Selbständige betrifft dies praktisch nur das Auto, da Sie wahrscheinlich keine grossen Maschinen oder Gebäudeunterhalt haben.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('7fb6f603-3b0b-4de3-94ed-0e78ecc2b290', 'Reisespesen', 'Z.B. Flug- oder Zug-Tickets.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('43d80419-4d4c-4d37-9638-63e0d7aae068', 'Repräsentation',
        'In diese Kategorie fallen beispielsweise Kundengeschenke oder Ausgaben für Geschäftsessen mit potenziellen Kunden oder Geschäftspartnern.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('c59452d2-27f3-42f6-8c01-a112896f33f8', 'Werbung/Marketing', 'Ausgaben für z.B. Visitenkarten, Inserate, Website oder Google AdWords.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('0d836985-8868-4b46-966e-d50195d112c3', 'Weiterbildung', 'Zum Beispiel besuchte Kurse oder Seminare.');
INSERT INTO public.category (id, bezeichnung, info)
VALUES ('f5b71e24-09c5-4265-bf64-512612df1a9b', 'Alle übrigen Geschäftsaufwände',
        'Alles, was nicht in die anderen Kategorien fällt. Normalerweise sollten hier nicht all zu viele Ausgaben anfallen. Zum Beispiel die jährlichen Kehrichtgrundgebühren für Betriebe der Gemeinde.');


--Accounting
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('36cc1778-f16a-4ebd-9fba-8000bf2c554c', '2022-01-02', null, null, 227.4, 'A-000001', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('40114429-f0ef-465c-b083-4839da12e706', '2022-02-01', null, null, 227.4, 'A-000002', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('be76bad1-6d27-4d8e-a3df-ea1a3276843e', '2022-03-01', null, null, 227.4, 'A-000003', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('4f70407f-8ac7-4a18-bf66-4173271368c8', '2022-04-01', null, null, 227.4, 'A-000004', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('078f46e0-479c-4ae0-9180-b1a601c56473', '2022-05-02', null, null, 227.4, 'A-000005', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('2e625ce5-5a9d-4d6c-800e-afa465638b82', '2022-06-01', null, null, 227.4, 'A-000006', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('b2a07e77-87bd-4815-a2f4-a2267e377e10', '2022-07-01', null, null, 227.4, 'A-000007', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('c75f0a68-0aa7-4355-a64a-aa6fb8815bfa', '2022-08-01', null, null, 227.4, 'A-000008', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('4eee32f5-dd1e-485b-8a04-69e90d80a051', '2022-09-01', null, null, 227.4, 'A-000009', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('c77b7942-c720-47e2-af52-cbe5a8da8b9f', '2022-10-02', null, null, 227.4, 'A-000010', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('6347cde5-3c5e-49d0-9729-80007d2f47d1', '2022-11-01', null, null, 227.4, 'A-000011', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('6f5a3528-e253-4e08-919c-b41119b8d3bd', '2022-12-01', null, null, 227.4, 'A-000012', 'Hiag AG', 'AUSGABE',
        '76c5c28e-79f2-4bea-8275-1e5258cf3db4', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('7c84dd34-06f5-4adf-8a54-87c4c8795c9a', '2022-02-22', null, null, 80, 'A-000013', 'Berufsverband Mitgliederbeitrag', 'AUSGABE',
        'f5b71e24-09c5-4265-bf64-512612df1a9b', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('fb822d44-ab9b-468d-ac3d-14f50af6a786', '2022-06-23', null, null, 299, 'A-000015', 'Nichtraucher Packet, Jan Hendrik Günther', 'AUSGABE',
        '0d836985-8868-4b46-966e-d50195d112c3', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('41d4fb5e-9dbf-4756-88ee-b265ed75242a', '2022-09-07', null, null, 99, 'A-000016', 'Abnehmpacket, Jan Hendrik Günther', 'AUSGABE',
        '0d836985-8868-4b46-966e-d50195d112c3', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('9f413a81-6d11-4986-8f73-045198b47ae4', '2022-10-19', null, null, 100, 'A-000017', 'Posttraumatisches Stresssyndrom, ASCA', 'AUSGABE',
        '0d836985-8868-4b46-966e-d50195d112c3', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('8b4b290f-a014-4686-833d-0f05120c3350', '2022-03-09', null, null, 68, 'A-000018', 'Internet, Yallo', 'AUSGABE',
        '6f7a2ab7-74d9-4d4b-9336-62f51ffa758a', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('10af272b-1bde-437b-bbe3-32656e6367e2', '2022-04-10', null, null, 34, 'A-000019', 'Internet, Yallo', 'AUSGABE',
        '6f7a2ab7-74d9-4d4b-9336-62f51ffa758a', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('cd0737a7-778e-4723-b75a-b83a8c3327f7', '2022-08-15', null, null, 34, 'A-000020', 'Internet, Yallo', 'AUSGABE',
        '6f7a2ab7-74d9-4d4b-9336-62f51ffa758a', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('204268d5-a8cb-4ccf-92dd-324af086ac1b', '2022-10-11', null, null, 68, 'A-000021', 'Internet, Yallo', 'AUSGABE',
        '6f7a2ab7-74d9-4d4b-9336-62f51ffa758a', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('bf9ee933-40f6-4ac1-98e4-3bb7ce5716b9', '2022-11-09', null, null, 34, 'A-000022', 'Internet, Yallo', 'AUSGABE',
        '6f7a2ab7-74d9-4d4b-9336-62f51ffa758a', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('45a9786f-da1f-43a7-90c9-5b197e1b3dc8', '2022-01-07', null, null, 50, 'A-000023', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('32745177-0df6-4e38-8af4-19aa13ad3063', '2022-01-21', null, null, 34.05, 'A-000024', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('7cb3ce50-7c79-4e78-9f1e-6ebf6edcd450', '2022-02-07', null, null, 50, 'A-000025', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('4559ade7-12d3-4606-8d79-d2f7285199f7', '2022-02-21', null, null, 33.9, 'A-000026', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('2e104241-ab77-44a2-98c3-ad0eae6bf2cb', '2022-02-10', null, null, 50, 'A-000027', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('fa6617b2-c514-4241-bc6c-13edfc4e8d7e', '2022-03-21', null, null, 25.85, 'A-000028', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('66d8f640-4a81-4e0a-9e80-fd43f85d35b7', '2022-04-07', null, null, 50, 'A-000029', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('24c1d972-057e-48e9-9504-04e70bba85b0', '2022-04-21', null, null, 34.1, 'A-000030', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('6c8717b9-bb50-48c0-b629-1af91d8f7f89', '2022-05-08', null, null, 50, 'A-000031', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('82255953-99d4-4c37-b641-c525c9a16f44', '2022-05-21', null, null, 31.25, 'A-000032', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('b19424af-c708-4a65-a55e-3624b0acdd51', '2022-06-07', null, null, 50, 'A-000033', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('0ea0af66-30f3-4b04-9514-b4f7ec53a6b0', '2022-06-21', null, null, 34.15, 'A-000034', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('0aa9dd8a-9af6-4130-9013-29640fc0be29', '2022-07-08', null, null, 50, 'A-000035', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('0c834174-8b61-4cc5-8c87-d4f16d9aec87', '2022-07-21', null, null, 31.2, 'A-000036', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('73445da3-ee1f-471c-a459-b13b6b9d9801', '2022-08-07', null, null, 50, 'A-000037', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('b75b4cb5-8d6a-456c-9263-f36f4ac59a41', '2022-08-21', null, null, 33.8, 'A-000038', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('e264ab29-b9c5-4891-bdf5-1f6315af12dd', '2022-09-07', null, null, 50, 'A-000039', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('ee8c989c-9510-4381-ba5e-a6225bb7e13f', '2022-09-21', null, null, 34.3, 'A-000040', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('3c7f7027-d4df-4638-a13a-eae37a99334e', '2022-09-27', null, null, 50, 'A-000041', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('598d9ed7-3d50-40ff-95f4-c876c9e1d7c9', '2022-11-16', null, null, 75, 'A-000043', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('c43c6dc1-b3f6-456d-a23c-07f2cccb06e4', '2022-11-21', null, null, 74.95, 'A-000042', 'Facebook', 'AUSGABE',
        'c59452d2-27f3-42f6-8c01-a112896f33f8', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('ca1cc601-bd1d-4dcc-8548-3713a4aa3a1a', '2022-01-02', null, null, 320, 'A-000044', 'Jahresgebühr, Socamed SA', 'AUSGABE',
        'f5b71e24-09c5-4265-bf64-512612df1a9b', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('11ff6749-898a-4098-95ce-f0d1b19e82ad', '2022-01-20', 180, '2022-01-20', null, 'E-000045', 'Bar, P.H.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('ee16e9d8-9d2c-4999-8bc4-0eccd17f8651', '2022-01-06', 180, '2022-01-06', null, 'E-000046', 'Rechnung, L.S.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('dafe7a28-174b-4df3-8059-0ff04d2272bd', '2022-07-05', 180, '2022-07-05', null, 'E-000047', 'Bar, C.M.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('48d26d89-4e3c-49f3-9300-5f00dc585158', '2022-08-10', 180, '2022-08-10', null, 'E-000048', 'Bar, C.M.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('2076d1e8-dde8-4e4a-b123-e5c911d84255', '2022-06-25', 180, '2022-06-25', null, 'E-000049', 'Bar, J.A.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('bf5cf390-65bd-48bd-a467-550ae9336204', '2022-07-01', 180, '2022-07-01', null, 'E-000050', 'Bar, J.A.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('1d1fd21f-5cbe-451f-ad86-34816280ce21', '2022-09-28', 180, '2022-09-28', null, 'E-000051', 'Bar, D.J.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('189698bf-2c8a-42bc-93bc-a16296fd4fb2', '2022-10-04', 180, '2022-10-04', null, 'E-000052', 'Bar, D.J.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('905006f5-0e34-4b56-8af4-3256c315449f', '2022-09-17', 180, '2022-09-17', null, 'E-000053', 'Bar, M.H.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('aa264b43-fbce-41d2-95be-dc3aa7b978e1', '2022-10-02', 180, '2022-10-02', null, 'E-000054', 'Bar, M.H.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('9d6109a2-3944-4621-be0d-aeaa41aa1172', '2022-02-11', 180, '2022-02-11', null, 'E-000055', 'Rechnung, L.S.', 'EINNAHME', null,
        '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
INSERT INTO public.accounting (id, buchungsdatum, einnahme, eingangsdatum, ausgabe, beleg_nr, text, accounting_type, category_id, tax_period_id)
VALUES ('770e358d-82d5-434b-b0ad-44cc59c19e06', '2022-04-27', null, null, 1950, 'A-000014', 'NLP-Practitioner-Ausbildung, Landsiedel NLP', 'AUSGABE',
        '0d836985-8868-4b46-966e-d50195d112c3', '3ff9b559-063d-46bd-89be-69aa1ed18cbf');
