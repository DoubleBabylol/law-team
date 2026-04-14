export interface RegionItem {
  label: string
  value: string
  children?: RegionItem[]
}

const regionData: RegionItem[] = [
  {
    label: '北京市', value: '北京市', children: [
      { label: '无', value: '无' },
      {
        label: '东城区', value: '东城区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '西城区', value: '西城区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '朝阳区', value: '朝阳区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '海淀区', value: '海淀区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '丰台区', value: '丰台区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '石景山区', value: '石景山区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '通州区', value: '通州区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '大兴区', value: '大兴区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '天津市', value: '天津市', children: [
      { label: '无', value: '无' },
      {
        label: '和平区', value: '和平区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '河东区', value: '河东区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '南开区', value: '南开区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '河西区', value: '河西区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '上海市', value: '上海市', children: [
      { label: '无', value: '无' },
      {
        label: '浦东新区', value: '浦东新区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '黄浦区', value: '黄浦区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '徐汇区', value: '徐汇区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '静安区', value: '静安区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '长宁区', value: '长宁区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '重庆市', value: '重庆市', children: [
      { label: '无', value: '无' },
      {
        label: '渝中区', value: '渝中区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '江北区', value: '江北区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '南岸区', value: '南岸区', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '河北省', value: '河北省', children: [
      { label: '无', value: '无' },
      {
        label: '石家庄市', value: '石家庄市', children: [
          { label: '无', value: '无' }, { label: '长安区', value: '长安区' }, { label: '桥西区', value: '桥西区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '唐山市', value: '唐山市', children: [
          { label: '无', value: '无' }, { label: '路北区', value: '路北区' }, { label: '路南区', value: '路南区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '保定市', value: '保定市', children: [
          { label: '无', value: '无' }, { label: '竞秀区', value: '竞秀区' }, { label: '莲池区', value: '莲池区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '山西省', value: '山西省', children: [
      { label: '无', value: '无' },
      {
        label: '太原市', value: '太原市', children: [
          { label: '无', value: '无' }, { label: '小店区', value: '小店区' }, { label: '迎泽区', value: '迎泽区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '大同市', value: '大同市', children: [
          { label: '无', value: '无' }, { label: '平城区', value: '平城区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '辽宁省', value: '辽宁省', children: [
      { label: '无', value: '无' },
      {
        label: '沈阳市', value: '沈阳市', children: [
          { label: '无', value: '无' }, { label: '和平区', value: '和平区' }, { label: '沈河区', value: '沈河区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '大连市', value: '大连市', children: [
          { label: '无', value: '无' }, { label: '中山区', value: '中山区' }, { label: '西岗区', value: '西岗区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '吉林省', value: '吉林省', children: [
      { label: '无', value: '无' },
      {
        label: '长春市', value: '长春市', children: [
          { label: '无', value: '无' }, { label: '南关区', value: '南关区' }, { label: '朝阳区', value: '朝阳区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '黑龙江省', value: '黑龙江省', children: [
      { label: '无', value: '无' },
      {
        label: '哈尔滨市', value: '哈尔滨市', children: [
          { label: '无', value: '无' }, { label: '道里区', value: '道里区' }, { label: '南岗区', value: '南岗区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '江苏省', value: '江苏省', children: [
      { label: '无', value: '无' },
      {
        label: '南京市', value: '南京市', children: [
          { label: '无', value: '无' }, { label: '玄武区', value: '玄武区' }, { label: '秦淮区', value: '秦淮区' }, { label: '鼓楼区', value: '鼓楼区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '苏州市', value: '苏州市', children: [
          { label: '无', value: '无' }, { label: '姑苏区', value: '姑苏区' }, { label: '虎丘区', value: '虎丘区' }, { label: '吴中区', value: '吴中区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '无锡市', value: '无锡市', children: [
          { label: '无', value: '无' }, { label: '梁溪区', value: '梁溪区' }, { label: '锡山区', value: '锡山区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '浙江省', value: '浙江省', children: [
      { label: '无', value: '无' },
      {
        label: '杭州市', value: '杭州市', children: [
          { label: '无', value: '无' }, { label: '上城区', value: '上城区' }, { label: '拱墅区', value: '拱墅区' }, { label: '西湖区', value: '西湖区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '宁波市', value: '宁波市', children: [
          { label: '无', value: '无' }, { label: '海曙区', value: '海曙区' }, { label: '江北区', value: '江北区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '温州市', value: '温州市', children: [
          { label: '无', value: '无' }, { label: '鹿城区', value: '鹿城区' }, { label: '龙湾区', value: '龙湾区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '安徽省', value: '安徽省', children: [
      { label: '无', value: '无' },
      {
        label: '合肥市', value: '合肥市', children: [
          { label: '无', value: '无' }, { label: '蜀山区', value: '蜀山区' }, { label: '庐阳区', value: '庐阳区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '福建省', value: '福建省', children: [
      { label: '无', value: '无' },
      {
        label: '福州市', value: '福州市', children: [
          { label: '无', value: '无' }, { label: '鼓楼区', value: '鼓楼区' }, { label: '台江区', value: '台江区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '厦门市', value: '厦门市', children: [
          { label: '无', value: '无' }, { label: '思明区', value: '思明区' }, { label: '湖里区', value: '湖里区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '江西省', value: '江西省', children: [
      { label: '无', value: '无' },
      {
        label: '南昌市', value: '南昌市', children: [
          { label: '无', value: '无' }, { label: '东湖区', value: '东湖区' }, { label: '西湖区', value: '西湖区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '山东省', value: '山东省', children: [
      { label: '无', value: '无' },
      {
        label: '济南市', value: '济南市', children: [
          { label: '无', value: '无' }, { label: '历下区', value: '历下区' }, { label: '市中区', value: '市中区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '青岛市', value: '青岛市', children: [
          { label: '无', value: '无' }, { label: '市南区', value: '市南区' }, { label: '市北区', value: '市北区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '河南省', value: '河南省', children: [
      { label: '无', value: '无' },
      {
        label: '郑州市', value: '郑州市', children: [
          { label: '无', value: '无' }, { label: '中原区', value: '中原区' }, { label: '金水区', value: '金水区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '湖北省', value: '湖北省', children: [
      { label: '无', value: '无' },
      {
        label: '武汉市', value: '武汉市', children: [
          { label: '无', value: '无' }, { label: '武昌区', value: '武昌区' }, { label: '江岸区', value: '江岸区' }, { label: '汉阳区', value: '汉阳区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '湖南省', value: '湖南省', children: [
      { label: '无', value: '无' },
      {
        label: '长沙市', value: '长沙市', children: [
          { label: '无', value: '无' }, { label: '岳麓区', value: '岳麓区' }, { label: '芙蓉区', value: '芙蓉区' }, { label: '天心区', value: '天心区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '广东省', value: '广东省', children: [
      { label: '无', value: '无' },
      {
        label: '广州市', value: '广州市', children: [
          { label: '无', value: '无' }, { label: '天河区', value: '天河区' }, { label: '越秀区', value: '越秀区' }, { label: '海珠区', value: '海珠区' }, { label: '白云区', value: '白云区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '深圳市', value: '深圳市', children: [
          { label: '无', value: '无' }, { label: '福田区', value: '福田区' }, { label: '南山区', value: '南山区' }, { label: '罗湖区', value: '罗湖区' }, { label: '宝安区', value: '宝安区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '东莞市', value: '东莞市', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '佛山市', value: '佛山市', children: [
          { label: '无', value: '无' }, { label: '禅城区', value: '禅城区' }, { label: '南海区', value: '南海区' }, { label: '顺德区', value: '顺德区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '广西壮族自治区', value: '广西壮族自治区', children: [
      { label: '无', value: '无' },
      {
        label: '南宁市', value: '南宁市', children: [
          { label: '无', value: '无' }, { label: '青秀区', value: '青秀区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '海南省', value: '海南省', children: [
      { label: '无', value: '无' },
      {
        label: '海口市', value: '海口市', children: [
          { label: '无', value: '无' }, { label: '龙华区', value: '龙华区' }, { label: '其他', value: '其他' }
        ]
      },
      {
        label: '三亚市', value: '三亚市', children: [
          { label: '无', value: '无' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '四川省', value: '四川省', children: [
      { label: '无', value: '无' },
      {
        label: '成都市', value: '成都市', children: [
          { label: '无', value: '无' }, { label: '锦江区', value: '锦江区' }, { label: '武侯区', value: '武侯区' }, { label: '高新区', value: '高新区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '贵州省', value: '贵州省', children: [
      { label: '无', value: '无' },
      {
        label: '贵阳市', value: '贵阳市', children: [
          { label: '无', value: '无' }, { label: '南明区', value: '南明区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '云南省', value: '云南省', children: [
      { label: '无', value: '无' },
      {
        label: '昆明市', value: '昆明市', children: [
          { label: '无', value: '无' }, { label: '五华区', value: '五华区' }, { label: '盘龙区', value: '盘龙区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '陕西省', value: '陕西省', children: [
      { label: '无', value: '无' },
      {
        label: '西安市', value: '西安市', children: [
          { label: '无', value: '无' }, { label: '雁塔区', value: '雁塔区' }, { label: '碑林区', value: '碑林区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '甘肃省', value: '甘肃省', children: [
      { label: '无', value: '无' },
      {
        label: '兰州市', value: '兰州市', children: [
          { label: '无', value: '无' }, { label: '城关区', value: '城关区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '青海省', value: '青海省', children: [
      { label: '无', value: '无' },
      {
        label: '西宁市', value: '西宁市', children: [
          { label: '无', value: '无' }, { label: '城东区', value: '城东区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '内蒙古自治区', value: '内蒙古自治区', children: [
      { label: '无', value: '无' },
      {
        label: '呼和浩特市', value: '呼和浩特市', children: [
          { label: '无', value: '无' }, { label: '赛罕区', value: '赛罕区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '西藏自治区', value: '西藏自治区', children: [
      { label: '无', value: '无' },
      {
        label: '拉萨市', value: '拉萨市', children: [
          { label: '无', value: '无' }, { label: '城关区', value: '城关区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '宁夏回族自治区', value: '宁夏回族自治区', children: [
      { label: '无', value: '无' },
      {
        label: '银川市', value: '银川市', children: [
          { label: '无', value: '无' }, { label: '兴庆区', value: '兴庆区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '新疆维吾尔自治区', value: '新疆维吾尔自治区', children: [
      { label: '无', value: '无' },
      {
        label: '乌鲁木齐市', value: '乌鲁木齐市', children: [
          { label: '无', value: '无' }, { label: '天山区', value: '天山区' }, { label: '其他', value: '其他' }
        ]
      }
    ]
  },
  {
    label: '香港特别行政区', value: '香港特别行政区', children: [
      { label: '无', value: '无' }
    ]
  },
  {
    label: '澳门特别行政区', value: '澳门特别行政区', children: [
      { label: '无', value: '无' }
    ]
  },
  {
    label: '台湾省', value: '台湾省', children: [
      { label: '无', value: '无' }
    ]
  }
]

export default regionData
