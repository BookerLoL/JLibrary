package main.util.metrics;
/*
 * For Strings
 * 
 * Levenshtein Distance / Edit Distance
 */
public class LevenshteinDistance {
	public static int distance(String str1, String str2) {
		final int rowLength = str1.length() + 1;
		final int colLength = str2.length() + 1;
		int[][] mem = new int[rowLength][colLength];
		
		for (int row = 1; row < rowLength; row++) {
			mem[row][0] = row;
		}
		
		for (int col = 1; col < colLength; col++) {
			mem[0][col] = col;
		}
		
		for (int row = 1; row < rowLength; row++) {
			for (int col = 1; col < colLength; col++) {
				int d = str1.charAt(row-1) == str2.charAt(col-1) ? 0 : 1;
				mem[row][col] = Math.min(mem[row][col-1] + 1, Math.min(mem[row-1][col] + 1, mem[row-1][col-1] + d));
			}
		}
		
		return mem[str1.length()][str2.length()];
	}
	
	public static void main(String[] args) {
		String input = "Neoplasm: any new and abnormal growth, in particular new growth of tissue in which the growth is uncontrolled and progressive\r\n" + 
		"Nephritis: see glomerulonephritis\r\n" + 
		"Nephrolithiasis: see renal stones\r\n" + 
		"Nephrology: the branch of medical science that deals with the kidneys\r\n" + 
		"Nephron: the structural and functional units of the kidney, numbering over a million in each kidney, which are capable of forming urine\r\n" + 
		"Nephropathy: any disease of the kidney\r\n" + 
		"Nephrosis: degeneration of the renal tubular epithelium\r\n" + 
		"Nephrotic syndrome: the excretion of large amounts of protein in the urine per day. This is characteristic of glomerular injury.\r\n" + 
		"Neurogenic bladder: a functional urinary tract obstruction caused by an interruption of the nerve supply to the bladder\r\n" + 
		"Oliguria: diminished excretion of urine (< 400 mL/d or 30 mL/h)\r\n" + 
		"Organ: structurally distinct part of the body which usually performs a particular function. Usually made up of several types of tissue in a very organised structure, e.g. kidney, heart, lungs, liver.\r\n" + 
		"Osteomalacia: inadequate or delayed mineralisation of the bone matrix in mature compact and spongy bone\r\n" + 
		"Peritoneal cavity: abdominal cavity that contains the intestines and other internal organs; lined by the peritoneum or peritoneal membrane\r\n" + 
		"Peritoneal dialysis (PD): treatment for kidney failure in which dialysis fluid is introduced into the peritoneal cavity to remove wastes and water from the blood\r\n" + 
		"Peritoneum: thin membrane that encloses the peritoneal cavity and surrounds the abdominal organs\r\n" + 
		"Peritonitis: inflammation of the peritoneum\r\n" + 
		"Phosphate binder: medication that binds with phosphate in the intestine causing some of the phosphate to be passed in the faeces\r\n" + 
		"Phosphate: mineral in the body fluids regulated by the kidneys. At normal levels, keeps bones and other parts strong and healthy. At high levels, causes itching and painful joints\r\n" + 
		"Plasma creatinine (PCr) concentration: a blood test which is directly related to GFR. When the GFR decreases, PCr increases\r\n" + 
		"Podocytes: specialised cells located in the glomerular epithelium. These foot-like cells interlock and form a network of intracellular clefts called filtration slits which filter the glomerular filtrate, forming the primary urine\r\n" + 
		"Polycystic kidney disease: inherited kidney disease that produces fluid-filled cysts in the kidneys that produce chronic renal failure over many years\r\n" + 
		"Potassium: mineral in the body fluids regulated by the kidneys. At normal levels, helps nerves, muscles and other cells work well. At high levels, may result in cardiac arrest or arrhythmias\r\n" + 
		"Protein: substance obtained from food which builds, repairs and maintains body tissues\r\n" + 
		"Pruritus: itching\r\n" + 
		"Purpura: a disease characterised by purple or livid spots on the skin or mucous membranes caused by blood being forced out of the blood vessels and into the surrounding tissue\r\n" + 
		"Pyuria: white blood cells in the urine\r\n" + 
		"Pyelonephritis: an infection of the renal pelvis and interstitium\r\n" + 
		"Recipient: person who receives (a new organ)\r\n" + 
		"Reflux nephropathy: kidney disease caused by the backflow of urine from the bladder up the ureters into the kidney tissue\r\n" + 
		"Renal arteries: arise as the fifth branches of the abdominal aorta and supply blood to the kidneys\r\n" + 
		"Renal cortex: area of the kidney that contains all the glomeruli and portions of the tubules\r\n" + 
		"Renal failure: loss of kidney function\r\n" + 
		"Renal insufficiency: decline in renal function to about 25% of normal or a GFR of 25-30 ml/min\r\n" + 
		"Renal pelvis: a hollow structure which is an extension of the upper end of the ureter\r\n" + 
		"Renal stones (also known as calculi, nephrolithiasis): masses of crystals and protein and are common causes of urinary tract obstruction in adults\r\n" + 
		"Renin: an enzyme which is produced, secreted, and stored by the kidneys, that plays a role in regulating blood pressure\r\n" + 
		"Renin-angiotensin system: a major hormonal regulator of renal blood flow, which can increase systemic arterial pressure and thus change renal blood flow.\r\n" + 
		"Septicaemia: the presence and persistence of pathogenic microorganisms or their toxins in the blood which affects the body as a whole (i.e. a systemic disease)\r\n" + 
		"Sodium: mineral in the body fluids regulated by the kidneys. Affects the level of water retained in the body tissues\r\n" + 
		"Staghorn calculi: large stones which grow in the pelvis and extend into the calyces to form branching stones\r\n" + 
		"Steroid: medication which reduces inflammation and is used to fight rejection\r\n" + 
		"Subclavian vein: blood vessel located underneath the collarbone sometimes used to provide access for haemodialysis\r\n" + 
		"Tissue typing: procedure to determine the degree of compatibility between lymphocytes (type of white blood cell) of a donor organ and a recipient\r\n" + 
		"Tissue: cells of a particular type massed together, e.g. blood, nerves\r\n" + 
		"Transplant: to transfer, as an organ or a portion of tissue, from one person to another\r\n" + 
		"Ultrafiltration: the process of producing a filtrate of protein-free plasma\r\n" + 
		"Uraemia: a syndrome of renal failure and includes elevated blood urea and creatinine levels accompanied by fatigue, anorexia, nausea and vomiting\r\n" + 
		"Urea: waste product from the breakdown of protein and the major constituent of urine along with water\r\n" + 
		"Ureterorenoscopy (URS): visual inspection of the interior of the ureter and kidney by means of a fiberoptic endoscope\r\n" + 
		"Ureter: tubular structure that transports urine form the kidney to the bladder\r\n" + 
		"Urethra: tubular structure which transports the urine from the bladder to the outside of the body\r\n" + 
		"Urethral syndrome: symptoms of cystitis, such as frequency, urgency and dysuria, but with negative urine cultures\r\n" + 
		"Urinalysis: test to measure the presence of protein, blood and other substances in the urine\r\n" + 
		"Urology: the branch of medicine dealing with the urinary system in females and the genitourinary system in males\r\n" + 
		"Vein: blood vessel returning blood to the heart\r\n" + 
		"Vitamin D: a fat-soluble chemical that is activated naturally in the body on exposure to sunlight\r\n" + 
		"access: In dialysis, the point on the body where a needle or catheter is inserted. (See also arteriovenous fistula, graft, and vascular access.)\r\n" + 
		"ACE Inhibitor: Angiotensin-Converting Enzyme Inhibitor. A medicine used to treat high blood pressure. ACE inhibitors can also help prevent or slow kidney damage.\r\n" + 
		"Acute: Acute often means urgent. An acute disease happens suddenly. It lasts a short time. Acute is the opposite of chronic, or long lasting.\r\n" + 
		"acute renal failure: Sudden and temporary loss of kidney function. \r\n" + 
		"acute tubular necrosis (ATN): A severe form of acute renal failure that develops in people with severe illnesses like infections or with low blood pressure. Patients may need dialysis. Kidney function often improves if the underlying disease is successfully treated.\r\n" + 
		"albuminuria: More than normal amounts of a protein called albumin in the urine. Albuminuria may be a sign of kidney disease.\r\n" + 
		"allograft: An organ or tissue transplant from one human to another.\r\n" + 
		"Alport syndrome: An inherited condition that results in kidney disease. It generally develops during early childhood and is more serious in boys than in girls. The condition can lead to end-stage renal disease, as well as hearing and vision problems. The common symptoms of this condition are chronic blood and protein in the urine.\r\n" + 
		"Amyloidosis: A condition in which a protein-like material builds up in one or more organs. This material cannot be broken down and interferes with the normal function of that organ. People who have been on dialysis for several years often develop amyloidosis because the artificial membranes used in dialysis fail to filter the protein-like material out of the blood.\r\n" + 
		"analgesic-associated kidney disease: Loss of kidney function that results from long-term use of analgesic (pain-relieving) medications. Analgesics that combine aspirin and acetaminophen are most dangerous to the kidneys.\r\n" + 
		"anemia: The condition of having too few red blood cells. Healthy red blood cells carry oxygen throughout the body. If the blood is low on red blood cells, the body does not get enough oxygen. People with anemia may be tired and pale and may feel their heartbeat change. Anemia is common in people with chronic kidney disease or those on dialysis. (See also erythropoietin.)\r\n" + 
		"antidiuretic hormone (ADH): A natural body chemical that slows down the urine flow. Some children who wet their beds regularly may lack normal amounts of antidiuretic hormone.\r\n" + 
		"anuria : A condition in which a person stops making urine.\r\n" + 
		"ARB: Angiotensin II Receptor Blocker/Inhibitor. A medicine used to treat high blood pressure. ARBs can also help prevent or slow kidney damage.\r\n" + 
		"arterial line: In hemodialysis (see dialysis), tubing that takes blood from the body to the dialyzer.\r\n" + 
		"arteriovenous (AV) fistula : Surgical connection of an artery directly to a vein, usually in the forearm, created in patients who will need hemodialysis . The AV fistula causes the vein to grow thicker, allowing the repeated needle insertions required for hemodialysis.\r\n" + 
		"artery : A blood vessel that carries blood away from the heart to the body. (See also vein.)\r\n" + 
		"artificial kidney: Another name for a dialyzer.\r\n" + 
		"autoimmune disease: A disease that occurs when the body’s immune system mistakenly attacks the body itself.\r\n" + 
		"biopsy : A procedure in which a tiny piece of a body part, such as the kidney or bladder, is removed for examination under a microscope.\r\n" + 
		"bladder: The balloon-shaped organ inside the pelvis that holds urine.\r\n" + 
		"Blood Pressure: Your heart pumps blood through tubes called arteries and veins. The pumped blood makes pressure inside your arteries. This is called blood pressure. When your blood pressure is checked, it tells how hard your heart is working to pump your blood. For people with diabetes, a normal blood pressure is less than 130/80.2\r\n" + 
		"blood urea nitrogen (BUN): A waste product in the blood that comes from the breakdown of food protein. The kidneys filter blood to remove urea. As kidney function decreases, the BUN level increases.\r\n" + 
		"calcium: A mineral that the body needs for strong bones and teeth. Calcium may form stones in the kidney.\r\n" + 
		"catheter: A tube inserted through the skin into a blood vessel or cavity to draw out body fluid or infuse fluid. In peritoneal dialysis, a catheter is used to infuse dialysis solution into the abdominal cavity and drain it out again.\r\n" + 
		"Cholesterol: A waxy, fat-like substance in your blood. Your body needs some cholesterol, but too much cholesterol can raise your risk for heart disease and kidney disease. A normal total cholesterol is less than 200.3\r\n" + 
		"chronic: Lasting a long time. Chronic diseases develop slowly. chronic kidney disease may develop over many years and lead to end-stage renal disease.\r\n" + 
		"chronic kidney disease: Slow and progressive loss of kidney function over several years, often resulting in permanent kidney failure. People with permanent kidney failure need dialysis or transplantation to replace the work of the kidneys.\r\n" + 
		"Congenital  nephrotic syndrome: A genetic kidney disease that develops before birth or in the first few months of life. Congenital nephrotic syndrome usually leads to end-stage renal disease and the need for dialysis or a kidney transplant by the second or third year of life.\r\n" + 
		"Creatinine: A type of waste in the blood that comes from using your muscles in everyday activities. Healthy kidneys clean creatinine from the blood. When your kidneys are not working, creatinine can build up in your blood.\r\n" + 
		"creatinine clearance: A test that measures how efficiently the kidneys remove creatinine and other wastes from the blood. Low creatinine clearance indicates impaired kidney function.\r\n" + 
		"cross-matching: Before a transplant, the donor’s blood is tested with the recipient’s blood to see whether they are compatible.\r\n" + 
		"cyst: An abnormal sac containing gas, fluid, or a semisolid material. Cysts may form in kidneys or in other parts of the body. (See also renal cysts.)\r\n" + 
		"cystine : An amino acid found in blood and urine. Amino acids are building blocks of protein. (See also cystine stone and cystinuria.)\r\n" + 
		"cystine stone: A rare form of kidney stone consisting of the amino acid cystine.\r\n" + 
		"cystinuria : A condition in which urine contains high levels of the amino acid cystine. If cystine does not dissolve in the urine, it can build up to form kidney stones.\r\n" + 
		"cystitis: Inflammation of the bladder, causing pain and a burning feeling in the pelvis or urethra.\r\n" + 
		"cystoscope: A tool for examining the bladder. The procedure is called cystoscopy .\r\n" + 
		"Diabetes insipidus: A condition characterized by frequent and heavy urination, excessive thirst, and an overall feeling of weakness. This condition may be caused by a defect in the pituitary gland or in the kidney. In diabetes insipidus, blood glucose levels are normal. (See also nephrogenic diabetes insipidus.)\r\n" + 
		"Diabetes: A disease that keeps the body from making or using insulin correctly. Your body needs insulin to get energy from sugar in the foods you eat. If your body canâ€™t make or use insulin correctly, sugar can build up in your blood and cause problems.\r\n" + 
		"Diabetes Educator: An important member of your healthcare team. Diabetes educators can teach you how to better control your diabetes.\r\n" + 
		"Diabetic Nephropathy: The medical name for kidney disease caused by diabetes.\r\n" + 
		"Dialysis: A way of cleaning waste and extra fluid from the blood once the kidneys have failed. hemodialysis : The use of a machine to clean wastes from the blood after the kidneys have failed. The blood travels through tubes to a dialyzer, which removes wastes and extra fluid. The cleaned blood then flows through another set of tubes back into the body.\r\n" + 
		"peritoneal dialysis: Cleaning the blood by using the lining of the abdominal cavity as a filter. A cleansing liquid, called dialysis solution, is drained from a bag into the abdomen. Fluids and wastes flow through the lining of the cavity and remain “trapped” in the dialysis solution. The solution is then drained from the abdomen, removing the extra fluids and wastes from the body. \r\n" + 
		"continuous ambulatory peritoneal dialysis (CAPD): The most common type of peritoneal dialysis. It needs no machine. With CAPD, the blood is always being cleaned. The dialysis solution passes from a plastic bag through the catheter and into the abdomen. The solution stays in the abdomen with the catheter sealed. After several hours, the person using CAPD drains the solution back into a disposable bag. Then the person refills the abdomen with fresh solution through the same catheter, to begin the cleaning process again.\r\n" + 
		"continuous cycling peritoneal dialysis (CCPD): A form of peritoneal dialysis that uses a machine. This machine automatically fills and drains the dialysis solution from the abdomen. A typical CCPD schedule involves three to five exchanges during the night while the person sleeps. During the day, the person using CCPD performs one exchange with a dwell time that lasts the entire day.\r\n" + 
		"dialysis solution: A cleansing liquid used in the two major forms of dialysis’hemodialysis and peritoneal dialysis. Dialysis solution contains dextrose (a sugar) and other chemicals similar to those in the body. Dextrose draws wastes and extra fluid from the body into the dialysis solution.\r\n" + 
		"dialyzer : A part of the hemodialysis machine. The dialyzer has two sections separated by a membrane. One section holds dialysis solution. The other holds the patient’s blood.\r\n" + 
		"Diastolic Pressure: Your blood pressure between heart beats. This is when the pressure is lowest.\r\n" + 
		"Dietician: An important member of your healthcare team. A dietician can help you manage your blood pressure through diet changes.\r\n" + 
		"Diuretic: A type of medicine that helps your body get rid of extra fluid. Having too much fluid in your body can raise your blood pressure. Diuretics are sometimes called â€œwater pillsâ€.\r\n" + 
		"donor: A person who offers blood, tissue, or an organ for transplantation.  In kidney transplantation, the donor may be someone who has just died or someone who is still alive, usually a relative.\r\n" + 
		"dry weight: The ideal weight for a person after a hemodialysis treatment. The weight at which a person’s blood pressure is normal and no swelling exists because all excess fluid has been removed.\r\n" + 
		"dwell time: In peritoneal dialysis , the amount of time a bag of dialysis solution remains in the patient’s abdominal cavity during an exchange.\r\n" + 
		"edema : Swelling caused by too much fluid in the body.\r\n" + 
		"electrolytes: Chemicals in the body fluids that result from the breakdown of salts, including sodium, potassium, magnesium, and chloride. The kidneys control the amount of electrolytes in the body. When the kidneys fail, electrolytes get out of balance, causing potentially serious health problems. Dialysis can correct this problem.\r\n" + 
		"end-stage renal disease (ESRD): Total and permanent kidney failure. When the kidneys fail, the body retains fluid and harmful wastes build up. A person with ESRD needs treatment to replace the work of the failed kidneys.\r\n" + 
		"erythropoietin: A hormone made by the kidneys to help form red blood cells. Lack of this hormone may lead to anemia.\r\n" + 
		"ESRD: See end-stage renal disease.\r\n" + 
		"Estimated Glomerular Filtration Rate (eGFR): A number based on your blood test for creatinine. It tells how well your kidneys are working. An eGFR less than 60 for 3 months or more may be a sign of kidney disease.1\r\n" + 
		"ESWL: See extracorporeal shockwave lithotripsy.\r\n" + 
		"exchange: In peritoneal dialysis, the draining of used dialysis solution from the abdomen, followed by refilling with a fresh bag of solution.\r\n" + 
		"extracorporeal shockwave lithotripsy (ESWL): A nonsurgical procedure using shock waves to break up kidney stones.\r\n" + 
		"fistula : See arteriovenous fistula.\r\n" + 
		"Glomerular filtration rate (GFR): A calculation of how efficiently the kidneys are filtering wastes from the blood. A traditional GFR calculation requires an injection into the bloodstream of a fluid that is later measured in a 24-hour urine collection. A modified GFR calculation requires only that the creatinine in a blood sample be measured. Each laboratory has its own normal range for measurements. Generally, the normal range for men is 97 to 137 mL/min/1.73 m2 of body surface area. The normal range for women is 88 to 128 mL/min/1.73 m2.\r\n" + 
		"glomeruli : The tiny blood vessels in your kidneys that filter your blood.\r\n" + 
		"glomerulonephritis : Inflammation of the glomeruli. Most often, it is caused by an autoimmune disease, but it can also result from infection.\r\n" + 
		"glomerulosclerosis : Scarring of the glomeruli. It may result from diabetes mellitus (diabetic glomerulosclerosis) or from deposits in parts of the glomeruli (focal segmental glomerulosclerosis). The most common signs of glomerulosclerosis are proteinuria and kidney failure.\r\n" + 
		"glomerulus : A tiny set of looping blood vessels in the nephron where blood is filtered in the kidney.\r\n" + 
		"Goodpasture syndrome: An uncommon disease that usually includes bleeding from the lungs, coughing up of blood, and inflammation of the kidneys that can lead to kidney failure. This condition is an autoimmune disease.\r\n" + 
		"Glucometer: A small machine that you can use to test your blood sugar at home.\r\n" + 
		"Glucose: The main sugar found in your blood. Your body turns many of the foods you eat into glucose. This is your bodyâ€™s main source of energy.\r\n" + 
		"Glucose Tablet: A small, chewable tablet made of glucose. If your blood sugar drops too low, you can eat a glucose tablet to help bring it back to a healthy range.\r\n" + 
		"graft: In hemodialysis, a vascular access surgically created using a synthetic tube to connect an artery to a vein. In transplantation, a graft is the transplanted organ or tissue.\r\n" + 
		"HDL: Also called high density lipoprotein or â€œgoodâ€ cholesterol. HDL carries cholesterol to the liver where it can be removed from the blood. An HDL level of more than 40 is good. An HDL level more than 60 is even better.3\r\n" + 
		"hematocrit: A measure that tells what portion of a blood sample consists of red blood cells. Low hematocrit suggests anemia or massive blood loss.\r\n" + 
		"hematuria : A condition in which urine contains blood or red blood cells.\r\n" + 
		"hemodialysis:\r\n" + 
		"Hemoglobin A1C (A1C): A blood test to check how your blood sugar has been over the last 2 or 3 months. Most people with diabetes should try to have an A1C less than 7%.2\r\n" + 
		"hemolytic uremic syndrome (HUS): A disease that affects the blood and blood vessels. It destroys red blood cells, cells that cause the blood to clot, and the lining of blood vessels. HUS is often caused by the Escherichia coli bacterium in contaminated food. People with HUS may develop acute renal failure.\r\n" + 
		"hormone: A natural chemical produced in one part of the body and released into the blood to trigger or regulate particular functions of the body. The kidney releases three hormones: erythropoietin, renin, and an active form of vitamin D that helps regulate calcium for bones.\r\n" + 
		"hydronephrosis: Swelling of the top of the ureter, usually because something is blocking the urine from flowing into or out of the bladder.\r\n" + 
		"hypercalciuria: Abnormally large amounts of calcium in the urine.\r\n" + 
		"Hyperoxaluria: Unusually large amounts of oxalate in the urine, leading to kidney stones.\r\n" + 
		"hypertension: High blood pressure, which can be caused either by too much fluid in the blood vessels or by narrowing of the blood vessels.\r\n" + 
		"IgA nephropathy: A kidney disorder caused by deposits of the protein immunoglobulin A (IgA) inside the glomeruli (filters) within the kidney. The IgA protein damages the glomeruli, leading to blood and protein in the urine, to swelling in the hands and feet, and sometimes to kidney failure.\r\n" + 
		"immune system: The body’s system for protecting itself from viruses and bacteria or any ‘foreign’ substances.\r\n" + 
		"immunosuppressant : A drug given to suppress the natural responses of the body’s immune system. Immunosuppressants are given to transplant patients to prevent organ rejection and to patients with autoimmune diseases like lupus.\r\n" + 
		"interstitial nephritis : Inflammation of the kidney cells that are not part of the fluid-collecting units, a condition that can lead to acute renal failure or chronic kidney disease.\r\n" + 
		"intravenous pyelogram: An x ray of the urinary tract. A dye is injected to make the kidneys, ureters, and bladder visible on the x ray and show any blockage in the urinary tract.\r\n" + 
		"Insulin: A hormone that helps your body turn the sugar you eat into energy. In diabetes, your body either doesnâ€™t make or use insulin correctly.\r\n" + 
		"kidney: One of two bean-shaped organs that filter wastes from the blood. The kidneys are located near the middle of the back. They create urine, which is delivered to the bladder through tubes called ureters.\r\n" + 
		"Kidney Disease: Permanent damage to the kidneys. The most common causes are diabetes and high blood pressure. If left untreated, kidney disease can lead to kidney failure.\r\n" + 
		"kidney failure: When the kidneys donâ€™t work well enough to clean your blood. A person with kidney failure will need dialysis or a kidney transplant to live.\r\n" + 
		"kidney stone: A stone that develops from crystals that form in urine and build up on the inner surfaces of the kidney, in the renal pelvis, or in the ureters.\r\n" + 
		"Kidney Transplant: When a healthy kidney from one person is placed in someone else whose kidneys have failed. A kidney transplant can come from a living donor or from someone who has just died.\r\n" + 
		"Kt/V : A measurement of dialysis dose. The measurement takes into account the efficiency of the dialyzer, the treatment time, and the total volume of urea in the body. \r\n" + 
		"LDL: Also called low density lipoprotein or â€œbadâ€ cholesterol. A high LDL level puts you more at risk for kidney disease, heart disease and stroke. A normal LDL level is less than 100.3\r\n" + 
		"lithotripsy: A method of breaking up kidney stones using shock waves or other means.\r\n" + 
		"lupus nephritis : Inflammation of the kidneys caused by an autoimmune disease called systemic lupus erythematosus. The condition can cause hematuria and proteinuria, and it may progress to end-stage renal disease.\r\n" + 
		"Medical Nutrition Therapy (MNT): Using nutrition to help control chronic conditions like diabetes, heart disease or kidney disease. MNT usually means working with a dietician to make health changes to your diet.\r\n" + 
		"membrane: A thin sheet or layer of tissue that lines a cavity or separates two parts of the body. A membrane can act as a filter, allowing some particles to pass from one part of the body to another while keeping others where they are. The artificial membrane in a dialyzer filters waste products from the blood.\r\n" + 
		"membranoproliferative glomerulonephritis: A disease that occurs primarily in children and young adults. Over time, inflammation leads to scarring in the glomeruli, causing proteinuria, hematuria, and sometimes chronic kidney disease or end-stage renal disease.\r\n" + 
		"membranous nephropathy: A disorder that hinders the kidneys’ ability to filter wastes from the blood because of harmful deposits on the glomerular membrane. Some cases of membranous nephropathy develop after an autoimmune disease or malignancy, but most develop without a known cause.\r\n" + 
		"nephrectomy: Surgical removal of a kidney.\r\n" + 
		"Nephrogenic diabetes insipidus: Constant thirst and frequent urination because the kidney tubules cannot respond to antidiuretic hormone. The result is an increase in urine formation and excessive urine flow.\r\n" + 
		"nephrolithiasis: See kidney stones.\r\n" + 
		"Nephrologist: A doctor who treats patients with kidney problems or hypertension.\r\n" + 
		"nephron: A tiny part of the kidneys. Each kidney is made up of about 1 million nephrons, which are the working units of the kidneys, removing wastes and extra fluids from the blood.\r\n" + 
		"nephropathy: Any disease of the kidney.\r\n" + 
		"nephrotic syndrome: A collection of symptoms that indicate kidney damage. Symptoms include high levels of protein in the urine, lack of protein in the blood, and high blood cholesterol.\r\n" + 
		"Nuclear scan: A test of the structure, blood flow, and function of the kidneys. The doctor injects a mildly radioactive solution into an arm vein and uses x rays to monitor its progress through the kidneys.\r\n" + 
		"oxalate: A chemical that combines with calcium in urine to form the most common type of kidney stone (calcium oxalate stone).\r\n" + 
		"pelvis: The bowl-shaped bone that supports the spine and holds up the digestive, urinary, and reproductive organs. The legs connect to the body at the pelvis.\r\n" + 
		"percutaneous nephrolithotomy: A method for removing kidney stones via keyhole surgery through the back.\r\n" + 
		"peritoneal cavity: The space inside the lower abdomen but outside the internal organs.\r\n" + 
		"peritoneal dialysis: See dialysis.\r\n" + 
		"peritoneum: The membrane lining the peritoneal cavity.\r\n" + 
		"peritonitis: Inflammation of the peritoneum, a common complication of peritoneal dialysis polycystic kidney disease (PKD): An inherited disorder characterized by many grape-like clusters of fluid-filled cysts that make both kidneys larger over time. These cysts take over and destroy working kidney tissue. PKD may cause chronic kidney disease and end-stage renal disease.\r\n" + 
		"potassium: A mineral found in the body and in many foods.\r\n" + 
		"proteinuria: The medical name for protein in your urine. This may be an early sign of kidney disease.\r\n" + 
		"pyelonephritis: An infection of the kidneys, usually caused by a germ that has traveled up through the urethra, bladder, and ureters from outside the body.\r\n" + 
		"renal: Of the kidneys. A renal disease is a disease of the kidneys. Renal failure means the kidneys have stopped working properly.\r\n" + 
		"renal agenesis: The absence or severe malformation of one or both kidneys.\r\n" + 
		"renal cell carcinoma : A type of kidney cancer.\r\n" + 
		"renal cysts : Abnormal fluid-filled sacs in the kidney that range in size from microscopic to much larger. Many simple cysts are harmless, while other types can seriously damage the kidneys.\r\n" + 
		"renal osteodystrophy: Weak bones caused by poorly working kidneys. Renal osteodystrophy is a common problem for people on dialysis who have high phosphate levels or insufficient vitamin D supplementation.\r\n" + 
		"renal pelvis: The basin into which the urine formed by the kidneys is excreted before it travels to the ureters and bladder.\r\n" + 
		"renal tubular acidosis: A defect in the kidneys that hinders their normal excretion of acids. Failure to excrete acids can lead to weak bones, kidney stones, and poor growth in children.\r\n" + 
		"renal vein thrombosis: Blood clots in the vessel that carries blood away from the kidney. This can occur in people with the nephrotic syndrome.\r\n" + 
		"renin: A hormone made by the kidneys that helps regulate the volume of fluid in the body and blood pressure.\r\n" + 
		"sodium: A mineral found in the body and in many foods.\r\n" + 
		"struvite stone: A type of kidney stone caused by infection.\r\n" + 
		"Systolic Pressure: Your blood pressure when your heart beats. This is when the pressure is highest.\r\n" + 
		"thrill: A vibration or buzz that can be felt in an arteriovenous fistula, an indication that the fistula is healthy.\r\n" + 
		"transplant: Replacement of a diseased organ with a healthy one. A kidney transplant may come from a living donor, usually a relative, or from someone who has just died.\r\n" + 
		"Triglycerides: A type of fat in the blood. Normal triglycerides are less than 150.3 High triglycerides can raise your risk of heart disease and kidney disease.\r\n" + 
		"ultrasound: A technique that bounces safe, painless sound waves off organs to create an image of their structure.\r\n" + 
		"urea: A waste product found in the blood and caused by the normal breakdown of protein in the liver. Urea is normally removed from the blood by the kidneys and then excreted in the urine. Urea accumulates in the body of people with renal failure.\r\n" + 
		"uremia: The illness associated with the buildup of urea in the blood because the kidneys are not working effectively. Symptoms include nausea, vomiting, loss of appetite, weakness, and mental confusion.\r\n" + 
		"ureteroscope : A tool for examining the bladder and ureters and for removing kidney stones through the urethra. The procedure is called ureteroscopy.\r\n" + 
		"ureters: Tubes that carry urine from the kidneys to the bladder.\r\n" + 
		"urethra: The tube that carries urine from the bladder to the outside of the body.\r\n" + 
		"uric acid stone: A kidney stone that may result from a diet high in animal protein. When the body breaks down this protein, uric acid levels rise and can form stones.\r\n" + 
		"Urine Albumin-to-Creatinine Ratio (UACR): A urine test that compares the amount of albumin to the amount of creatinine in your urine. A normal UACR is less than 30.1\r\n" + 
		"urinalysis : A test of a urine sample that can reveal many problems of the urinary system and other body systems. The sample may be observed for color, cloudiness, and concentration; signs of drug use; chemical composition, including sugar; the presence of protein, blood cells, or germs; or other signs of disease.\r\n" + 
		"urinary tract: The system that takes wastes from the blood and carries them out of the body in the form of urine. The urinary tract includes the kidneys, renal pelvises, ureters, bladder, and urethra.\r\n" + 
		"urinary tract infection (UTI): An illness caused by harmful bacteria growing in the urinary tract.\r\n" + 
		"urinate : To release urine from the bladder to the outside.\r\n" + 
		"Urine: Liquid waste product filtered from the blood by the kidneys, stored in the bladder, and expelled from the body through the urethra by the act of voiding or urinating. \r\n" + 
		"urolithiasis: Stones in the urinary tract.\r\n" + 
		"URR (urea reduction ratio): A blood test that compares the amount of blood urea nitrogen before and after dialysis to measure the effectiveness of the dialysis dose.\r\n" + 
		"vascular access: A general term to describe the area on the body where blood is drawn for circulation through a hemodialysis circuit. A vascular access may be an arteriovenous fistula, a graft, or a catheter.\r\n" + 
		"Vasculitis: Inflammation of the blood vessel walls. This can cause rash and disease in multiple organs of the body, including the kidneys.\r\n" + 
		"vein: A blood vessel that carries blood toward the heart.\r\n" + 
		"venous line: In hemodialysis, tubing that carries blood from the dialyzer back to the body.\r\n" + 
		"vesicoureteral reflux: An abnormal condition in which urine backs up into the ureters, and occasionally into the kidneys, raising the risk of infection.\r\n" + 
		"void: To urinate, empty the bladder.\r\n" + 
		"Wegener’s granulomatosis : An autoimmune disease that damages the blood vessels and causes disease in the lungs, upper respiratory tract, and kidneys.\r\n";
		
		String[] splitted = input.split("\\r\\n");
		for (int i = 0; i < splitted.length; i++) {
			System.out.println(splitted[i].substring(0, splitted[i].indexOf(":")));
		}
	}
}
