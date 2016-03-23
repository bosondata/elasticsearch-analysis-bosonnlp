# 玻森数据中文分析器Elasticsearch插件 (Beta版)

##概述
Elasticsearch 是一个基于 Lucene 的强大搜索服务器，也是企业最受欢迎的搜索引擎之一。但是 ES 本身对中文分词和搜索比较局限。因为内置的分析器在处理中文分词时，只有两种方式：一种是单字（unigrams）形式，即简单粗暴的将中文的每一个汉字作为一个词（token）分开；另一种是两字（bigrams）的，也就是任意相邻的两个汉字作为一个词分开。这两种方式都不能很好的满足现在的中文分词需求，进而影响了搜索结果。因此玻森数据开发了一款基于玻森中文分词的 Elasticsearch 的插件（BosonNLP Analysis for Elasticsearch）方便大家准确的使用中文搜索。

##安装

###依赖
Elasticsearch 官网安装说明 https://www.elastic.co/guide/en/elasticsearch/guide/1.x/_installing_elasticsearch.html

###选择插件版本
其对应的版本号和插件版本号如下：

| BosonNLP version | ES Version |
| :--------------: | :---------:|
| 1.3.0-beta       | 2.2.0      |
| 1.2.2-beta       | 2.1.2      |
| 1.2.1-beta       | 2.1.1      |
| 1.2.0-beta       | 2.1.0      |
| 1.1.0-beta       | 2.0.0      |
| 1.0.0-beta       | 1.7.x      |

###安装插件
现在提供以下两种方式安装插件。

####方法一
```markdown
$ sudo bin/plugin install https://github.com/bosondata/elasticsearch-analysis-bosonnlp/releases/download/1.3.0-beta/elasticsearch-analysis-bosonnlp-1.3.0-beta.zip
```
####方法二

1. 构建项目包

    下载玻森中文分析器项目到本地，并在项目根目录下通过 Maven 构建项目包：

    ```
    mvn clean package
    ```
    
    构建后的项目包`elasticsearch-analysis-bosonnlp-{version}.zip`在`target/releases/`生成。

2. 安装插件

   通过 Elasticsearch 的 plugin 加载插件，在 Elasticsearch 根目录执行以下命令即可：
 
    ```
    $ sudo bin/plugin install file:/root/path/to/your/elasticsearch-analysis-bosonnlp-{version}.zip
    ```

###设置

运行 Elasticsearch 之前需要在 config 文件夹中修改`elasticsearch.yml`来定义使用玻森中文分析器，并填写玻森 API_TOKEN 以及玻森分词 API 的地址，即在该文件结尾处添加：

```makefile
index:
  analysis:
    analyzer:
      bosonnlp:
          type: bosonnlp
          API_URL: http://api.bosonnlp.com/tag/analysis
          # You MUST give the API_TOKEN value, otherwise it doesn't work
          API_TOKEN: *PUT YOUR API TOKEN HERE*
          # Please uncomment if you want to specify ANY ONE of the following
          # areguments, otherwise the DEFAULT value will be used, i.e.,
          # space_mode is 0,
          # oov_level is 3,
          # t2s is 0,
          # special_char_conv is 0.
          # More detials can be found in bosonnlp docs:
          # http://docs.bosonnlp.com/tag.html
          #
          #
          # space_mode: put your value here(range from 0-3)
          # oov_level: put your value here(range from 0-4)
          # t2s: put your value here(range from 0-1)
          # special_char_conv: put your value here(range from 0-1)
```
**需要注意的是**

1. `必须在 API_URL 填写给定的分词地址以及在API_TOKEN：*PUT YOUR API TOKEN HERE* 中填写给定的玻森数据API_TOKEN`，否则无法使用玻森中文分析器。该 API_TOKEN 是[注册玻森数据账号](http://bosonnlp.com/)所获得。

2. 如果配置文件中已经有配置过其他的 analyzer，请直接在 analyzer 下如上添加 bosonnlp analyzer。

3. 如果有多个 node 并且都需要 BosonNLP 的分词插件，则每个 node 下的 yml 文件都需要如上安装和设置。

4. 另外，玻森中文分词还提供了4个参数（*space_mode*，*oov_level*，*t2s*，*special_char_conv*）可满足不同的分词需求。如果取默认值，则无需任何修改；否则，可取消对应参数的注释并赋值。

> 例：需开启繁体转换成简体（*t2s*）功能，则取消*t2s*的注释并赋值。
```makefile
 t2s: 1
```

更多关于玻森中文分词参数的信息，可以在此[了解](http://docs.bosonnlp.com/tag.html)。

设置完之后就可以运行 Elasticsearch 了，如果对该设置有新的改动，需要重启 Elasticsearch 才可生效。

###测试

####分词测试
运行 Elasiticsearch

显示插件加载成功
```
...
[time][INFO ][plugins] [Gaza] loaded [analysis-bosonnlp]
...
```
建立 index
```markdown
curl -XPUT 'localhost:9200/test'
```
测试分析器是否配置成功
```markdown
curl -XGET 'localhost:9200/test/_analyze?analyzer=bosonnlp&pretty' -d '这是玻森数据分词的测试'
```
结果
```json
{
  "tokens" : [ {
    "token" : "这",
    "start_offset" : 0,
    "end_offset" : 1,
    "type" : "word",
    "position" : 0
  }, {
    "token" : "是",
    "start_offset" : 1,
    "end_offset" : 2,
    "type" : "word",
    "position" : 1
  }, {
    "token" : "玻森",
    "start_offset" : 2,
    "end_offset" : 4,
    "type" : "word",
    "position" : 2
  }, {
    "token" : "数据",
    "start_offset" : 4,
    "end_offset" : 6,
    "type" : "word",
    "position" : 3
  }, {
    "token" : "分词",
    "start_offset" : 6,
    "end_offset" : 8,
    "type" : "word",
    "position" : 4
  }, {
    "token" : "的",
    "start_offset" : 8,
    "end_offset" : 9,
    "type" : "word",
    "position" : 5
  }, {
    "token" : "测试",
    "start_offset" : 9,
    "end_offset" : 11,
    "type" : "word",
    "position" : 6
  } ]
}

```

####搜索测试
建立 mapping
```markdown
curl -XPUT 'localhost:9200/test/text/_mapping' -d'
{
  "text": {
    "properties": {
      "content": {
        "type": "string", 
        "analyzer": "bosonnlp", 
        "search_analyzer": "bosonnlp"
      }
    }
  }
}
```
输入数据
```markdown
curl -XPUT 'localhost:9200/test/text/1' -d'
{"content": "美称中国武器商很神秘 花巨资海外参展却一言不发"}
'
```
```markdown
curl -XPUT 'localhost:9200/test/text/2' -d'
{"content": "复旦发现江浙沪儿童体内普遍有兽用抗生素"}
'
```
```markdown
curl -XPUT 'localhost:9200/test/text/3' -d'
{"content": "37年后重启顶层设计 中国未来城市发展料现四大变化"}
'
```
查询搜索
```markdown
curl -XPOST 'localhost:9200/test/text/_search?pretty'  -d'
{
  "query" : { "term" : { "content" : "中国" }}
}
'
```
结果
```json
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 2,
    "max_score" : 0.076713204,
    "hits" : [ {
      "_index" : "test",
      "_type" : "text",
      "_id" : "1",
      "_score" : 0.076713204,
      "_source":
{
 "content": "美称中国武器商很神秘 花巨资海外参展却一言不发"}
    }, {
      "_index" : "test",
      "_type" : "text",
      "_id" : "3",
      "_score" : 0.076713204,
      "_source":
{
 "content": "37年后重启顶层设计 中国未来城市发展料现四大变化"}
    } ]
  }
}
```
查询搜索
```markdown
curl -XPOST 'localhost:9200/test/text/_search?pretty'  -d'
{
  "query" : { "term" : { "content" : "国武" }}
}'
```
结果
```json
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 0,
    "max_score" : null,
    "hits" : [ ]
  }
}

```
查询搜索
```markdown
curl -XPOST 'localhost:9200/test/text/_search?pretty'  -d'
{
  "query" : { "term" : { "content" : "国" }}
}'
```
结果
```json
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 0,
    "max_score" : null,
    "hits" : [ ]
  }
}

```
如果用 ES 默认的分析器（Standard Analyzer）去查询，得到如下结果：

查询搜索
```markdown
curl -XPOST 'localhost:9200/test/text/_search?pretty'  -d'
{
  "query" : { "term" : { "content" : "国" }}
}'
```
结果
```json
{
  "took" : 8,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 2,
    "max_score" : 0.057534903,
    "hits" : [ {
      "_index" : "test3",
      "_type" : "text",
      "_id" : "1",
      "_score" : 0.057534903,
      "_source":
{"content": "美称中国武器商很神秘 花巨资海外参展却一言不发"}
    }, {
      "_index" : "test3",
      "_type" : "text",
      "_id" : "3",
      "_score" : 0.057534903,
      "_source":
{"content": "37年后重启顶层设计 中国未来城市发展料现四大变化"}

    } ]
  }
}

```
查询搜索
```markdown
curl -XPOST 'localhost:9200/test3/text/_search?pretty' -d '
{
 "query": {"term":{"content":"中国"}}
}'
```
结果
```json
{
  "took" : 14,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 0,
    "max_score" : null,
    "hits" : [ ]
  }
}

```

###配置 Token Filter
现有的 BosonNLP 分析器没有内置 token filter，如果有过滤 Token 的需求，可以利用 BosonNLP Tokenizer 和 ES 提供的 token filter 搭建定制分析器。

####步骤
配置定制的 analyzer 有以下三个步骤：

- 添加 BosonNLP tokenizer

在 `elasticsearch.yml` 文件中 analysis 下添加 tokenizer， 并在 tokenizer 中添加 BosonNLP tokenizer 的配置：
```makefile
index:
  analysis:
    analyzer:
      ...
    tokenizer:
      bosonnlp:
          type: bosonnlp
          API_URL: http://api.bosonnlp.com/tag/analysis
          # You MUST give the API_TOKEN value, otherwise it doesn't work
          API_TOKEN: *PUT YOUR API TOKEN HERE*
          # Please uncomment if you want to specify ANY ONE of the following
          # areguments, otherwise the DEFAULT value will be used, i.e.,
          # space_mode is 0,
          # oov_level is 3,
          # t2s is 0,
          # special_char_conv is 0.
          # More detials can be found in bosonnlp docs:
          # http://docs.bosonnlp.com/tag.html
          #
          #
          # space_mode: put your value here(range from 0-3)
          # oov_level: put your value here(range from 0-4)
          # t2s: put your value here(range from 0-1)
          # special_char_conv: put your value here(range from 0-1)
```

**同样需要注意的是**

1. `必须在 API_URL 中填写给定的分词地址以及在 API_TOKEN：*PUT YOUR API TOKEN HERE* 中填写给定的玻森数据API_TOKEN`，否则无法使用玻森 tokenizer。
2. 如果配置文件中已经有配置过其他的 tokenizer，请直接在 tokenizer 下如上添加 bosonnlp tokenizer。
3. 如果需要改动参数的默认值，请可取消对应参数的注释并赋值。

- 添加 token filter

在 `elasticsearch.yml` 文件中 analysis 下添加 filter， 并在 filter 中添加所需 filter 的配置（下面例子中，我们以 lowercase filter 为例）：
```makefile
index:
  analysis:
    analyzer:
      ...
    tokenizer:
      ...
    filter:
      lowercase:
          type: lowercase

```

- 添加定制的 anayzer

在 `elasticsearch.yml` 文件中 analysis 下添加 analyzer， 并在 analyzer 中添加定制的 analyzer 的配置（下面例子中，我们把定制的 analyzer 命名为 filter_bosonnlp）：
```markdown
index:
  analysis:
    analyzer:
      ...
      filter_bosonnlp:
          type: custom
          tokenizer: bosonnlp
          filter: [lowercase]
```
如有其他想要添加的 filter，可以在配置完 filter 之后在上述 filter：[] 列表中添加，以逗号隔开。
   
附上完整的定制 analyzer 配置：
```makefile
index:
  analysis:
    analyzer:
      filter_bosonnlp:
          type: custom
          tokenizer: bosonnlp
          filter: [lowercase]
    tokenizer:
      bosonnlp:
          type: bosonnlp
          API_URL: http://api.bosonnlp.com/tag/analysis
          # You MUST give the API_TOKEN value, otherwise it doesn't work
          API_TOKEN: *PUT YOUR API TOKEN HERE*
          # Please uncomment if you want to specify ANY ONE of the following 
          # areguments, otherwise the DEFAULT value will be used, i.e., 
          # space_mode is 0, 
          # oov_level is 3,
          # t2s is 0,
          # special_char_conv is 0.
          # More detials can be found in bosonnlp docs:
          # http://docs.bosonnlp.com/tag.html
          #
          #
          # space_mode: put your value here(range from 0-3)
          # oov_level: put your value here(range from 0-4)
          # t2s: put your value here(range from 0-1)
          # special_char_conv: put your value here(range from 0-1)
    filter:
      lowercase:
          type: lowercase

```
## 注意
由于 ES 搜索内核 Lucene 索引文件的设计结构所限，每个文档的每个字段必须单独分析, 无法采用 BosonNLP 的批处理调用，从而在 Network IO 上会有较大的时间开销。
