
CREATE TABLE "document_by_id" (
   "documentId" text,
   "applicationId" text,
   "category" text,
   "subCategory" text,
   "content" text,
   "createdAt" timestamp,
   PRIMARY KEY ("documentId","category", "applicationId")
) WITH CLUSTERING ORDER BY ("category" ASC);

