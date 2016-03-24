# Create index
curl -XPUT 'localhost:9200/bosonnlp_test'

# Create mapping
curl -XPUT 'localhost:9200/bosonnlp_test/text/_mapping?ignore_conflicts=true' -d '{
  "text":{
     "properties": {
        "content": {
            "type": "string",
            "term_vector": "with_positions_offsets",
            "analyzer": "bosonnlp",
            "search_analyzer": "bosonnlp"
        }
     }
  }
}'

# Index documents
curl -XPUT 'localhost:9200/bosonnlp_test/text/1?pretty' -d '{
    "content": "美称中国武器商很神秘 花巨资海外参展却一言不发"
}'

curl -XPUT 'localhost:9200/bosonnlp_test/text/2?pretty' -d '{
    "content": "在第一界国际锦标赛中 国家代表李雷勇夺冠军"
}'

curl -XPUT 'localhost:9200/bosonnlp_test/text/3?pretty' -d '{
    "content": "国武公司近日上市"
}'
