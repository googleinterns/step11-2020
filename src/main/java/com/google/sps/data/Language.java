// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents a user's language. Every UserAccount object stores an instance of this
 * enum.
 *
 * @author guptamudit
 * @version 1.0
 */
public enum Language {
  AB("Abkhaz", "аҧсуа"),
  AA("Afar", "Afaraf"),
  AF("Afrikaans", "Afrikaans"),
  AK("Akan", "Akan"),
  SQ("Albanian", "Shqip"),
  AM("Amharic", "አማርኛ"),
  AR("Arabic", "العربية"),
  AN("Aragonese", "Aragonés"),
  HY("Armenian", "Հայերեն"),
  AS("Assamese", "অসমীয়া"),
  AV("Avaric", "авар мацӀ"),
  AE("Avestan", "avesta"),
  AY("Aymara", "aymar aru"),
  AZ("Azerbaijani", "azərbaycan dili"),
  BM("Bambara", "bamanankan"),
  BA("Bashkir", "башҡорт теле"),
  EU("Basque", "euskara"),
  BE("Belarusian", "Беларуская"),
  BN("Bengali", "বাংলা"),
  BH("Bihari", "भोजपुरी"),
  BI("Bislama", "Bislama"),
  BS("Bosnian", "bosanski jezik"),
  BR("Breton", "brezhoneg"),
  BG("Bulgarian", "български език"),
  MY("Burmese", "ဗမာစာ"),
  CA("Catalan", "Català"),
  CH("Chamorro", "Chamoru"),
  CE("Chechen", "нохчийн мотт"),
  NY("Chichewa", "chiCheŵa"),
  ZH("Chinese", "中文 (Zhōngwén)"),
  CV("Chuvash", "чӑваш чӗлхи"),
  KW("Cornish", "Kernewek"),
  CO("Corsican", "corsu"),
  CR("Cree", "ᓀᐦᐃᔭᐍᐏᐣ"),
  HR("Croatian", "hrvatski"),
  CS("Czech", "česky"),
  DA("Danish", "dansk"),
  DV("Divehi", "ދިވެހި"),
  NL("Dutch", "Nederlands"),
  EN("English", "English"),
  EO("Esperanto", "Esperanto"),
  ET("Estonian", "eesti"),
  EE("Ewe", "Eʋegbe"),
  FO("Faroese", "føroyskt"),
  FJ("Fijian", "vosa Vakaviti"),
  FI("Finnish", "suomi"),
  FR("French", "français"),
  FF("Fula", "Fulfulde"),
  GL("Galician", "Galego"),
  KA("Georgian", "ქართული"),
  DE("German", "Deutsch"),
  EL("Greek", "Ελληνικά"),
  GN("Guaraní", "Avañeẽ"),
  GU("Gujarati", "ગુજરાતી"),
  HT("Haitian", "Kreyòl ayisyen"),
  HA("Hausa", "Hausa"),
  HE("Hebrew", "עברית"),
  HZ("Herero", "Otjiherero"),
  HI("Hindi", "हिन्दी"),
  HO("Hiri Motu", "Hiri Motu"),
  HU("Hungarian", "Magyar"),
  ID("Indonesian", "Bahasa Indonesia"),
  IE("Interlingue", "Interlingue"),
  GA("Irish", "Gaeilge"),
  IG("Igbo", "Asụsụ Igbo"),
  IK("Inupiaq", "Iñupiaq"),
  IO("Ido", "Ido"),
  IS("Icelandic", "Íslenska"),
  IT("Italian", "Italiano"),
  IU("Inuktitut", "ᐃᓄᒃᑎᑐᑦ"),
  JA("Japanese", "日本語 (にほんご／にっぽんご)"),
  JV("Javanese", "basa Jawa"),
  KL("Kalaallisut", "kalaallisut"),
  KN("Kannada", "ಕನ್ನಡ"),
  KR("Kanuri", "Kanuri"),
  KS("Kashmiri", "कश्मीरी"),
  KK("Kazakh", "Қазақ тілі"),
  KM("Khmer", "ភាសាខ្មែរ"),
  KI("Kikuyu", "Gĩkũyũ"),
  RW("Kinyarwanda", "Ikinyarwanda"),
  KY("Kirghiz", "кыргыз тили"),
  KV("Komi", "коми кыв"),
  KG("Kongo", "KiKongo"),
  KO("Korean", "한국어 (韓國語)"),
  KU("Kurdish", "Kurdî"),
  KJ("Kwanyama", "Kuanyama"),
  LA("Latin", "latine"),
  LB("Luxembourgish", "Lëtzebuergesch"),
  LG("Luganda", "Luganda"),
  LI("Limburgish", "Limburgs"),
  LN("Lingala", "Lingála"),
  LO("Lao", "ພາສາລາວ"),
  LT("Lithuanian", "lietuvių kalba"),
  LU("Luba-Katanga", ""),
  LV("Latvian", "latviešu valoda"),
  GV("Manx", "Gaelg"),
  MK("Macedonian", "македонски јазик"),
  MG("Malagasy", "Malagasy fiteny"),
  MS("Malay", "bahasa Melayu"),
  ML("Malayalam", "മലയാളം"),
  MT("Maltese", "Malti"),
  MI("Māori", "te reo Māori"),
  MR("Marathi (Marāṭhī)", "मराठी"),
  MH("Marshallese", "Kajin M̧ajeļ"),
  MN("Mongolian", "монгол"),
  NA("Nauru", "Ekakairũ Naoero"),
  NV("Navajo", "Diné bizaad"),
  NB("Norwegian Bokmål", "Norsk bokmål"),
  ND("North Ndebele", "isiNdebele"),
  NE("Nepali", "नेपाली"),
  NG("Ndonga", "Owambo"),
  NN("Norwegian Nynorsk", "Norsk nynorsk"),
  NO("Norwegian", "Norsk"),
  II("Nuosu", "ꆈꌠ꒿ Nuosuhxop"),
  NR("South Ndebele", "isiNdebele"),
  OC("Occitan", "Occitan"),
  OJ("Ojibwe", "ᐊᓂᔑᓈᐯᒧᐎᓐ"),
  CU("Old Church Slavonic", "ѩзыкъ словѣньскъ"),
  OM("Oromo", "Afaan Oromoo"),
  OR("Oriya", "ଓଡ଼ିଆ"),
  OS("Ossetian", "ирон æвзаг"),
  PA("Panjabi", "ਪੰਜਾਬੀ"),
  PI("Pali", "पाऴि"),
  FA("Persian", "فارسی"),
  PL("Polish", "polski"),
  PS("Pashto", "پښتو"),
  PT("Portuguese", "Português"),
  QU("Quechua", "Runa Simi"),
  RM("Romansh", "rumantsch grischun"),
  RN("Kirundi", "kiRundi"),
  RO("Romanian", "română"),
  RU("Russian", "русский язык"),
  SA("Sanskrit (Saṁskṛta)", "संस्कृतम्"),
  SC("Sardinian", "sardu"),
  SD("Sindhi", "सिन्धी"),
  SE("Northern Sami", "Davvisámegiella"),
  SM("Samoan", "gagana faa Samoa"),
  SG("Sango", "yângâ tî sängö"),
  SR("Serbian", "српски језик"),
  GD("Scottish Gaelic", "Gàidhlig"),
  SN("Shona", "chiShona"),
  SI("Sinhala", "සිංහල"),
  SK("Slovak", "slovenčina"),
  SL("Slovene", "slovenščina"),
  SO("Somali", "Soomaaliga"),
  ST("Southern Sotho", "Sesotho"),
  ES("Spanish", "español"),
  SU("Sundanese", "Basa Sunda"),
  SW("Swahili", "Kiswahili"),
  SS("Swati", "SiSwati"),
  SV("Swedish", "svenska"),
  TA("Tamil", "தமிழ்"),
  TE("Telugu", "తెలుగు"),
  TG("Tajik", "тоҷикӣ"),
  TH("Thai", "ไทย"),
  TI("Tigrinya", "ትግርኛ"),
  BO("Tibetan Standard", "བོད་ཡིག"),
  TK("Turkmen", "Türkmen"),
  TL("Tagalog", "Wikang Tagalog"),
  TN("Tswana", "Setswana"),
  TO("Tonga (Tonga Islands)", "faka Tonga"),
  TR("Turkish", "Türkçe"),
  TS("Tsonga", "Xitsonga"),
  TT("Tatar", "татарча"),
  TW("Twi", "Twi"),
  TY("Tahitian", "Reo Tahiti"),
  UG("Uighur", "Uyƣurqə"),
  UK("Ukrainian", "українська"),
  UR("Urdu", "اردو"),
  UZ("Uzbek", "zbek"),
  VE("Venda", "Tshivenḓa"),
  VI("Vietnamese", "Tiếng Việt"),
  VO("Volapük", "Volapük"),
  WA("Walloon", "Walon"),
  CY("Welsh", "Cymraeg"),
  WO("Wolof", "Wollof"),
  FY("Western Frisian", "Frysk"),
  XH("Xhosa", "isiXhosa"),
  YI("Yiddish", "ייִדיש"),
  YO("Yoruba", "Yorùbá"),
  ZA("Zhuang", "Saɯ cueŋƅ");

  private String longName;
  private String nativeName;

  private Language(String longName, String nativeName) {
    this.longName = longName;
    this.nativeName = nativeName;
  }

  public String getLongName() {
    return longName;
  }

  public String getNativeName() {
    return nativeName;
  }

  public static Collection<Language> valuesSorted() {
    List<Language> values = Arrays.asList(Language.values());
    values.sort(Comparator.comparing(Language::getLongName));
    return values;
  }
}
