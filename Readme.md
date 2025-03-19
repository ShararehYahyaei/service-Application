1 get all category
--- 
curl -X GET "http://localhost:8081/api/v1/category/getAllCategories" \
-H "Accept: application/json" \
-H "Authorization: Bearer YOUR_ACCESS_TOKEN"


2 create category

curl -X POST "http://localhost:8081/api/v1/category/create" \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
-d '{
"name": "نظافت و بهداشت"
}'
