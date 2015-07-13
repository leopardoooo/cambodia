/**
 * @author zhangdaiping@vip.qq.com
 * @version 1.4.0 (30/5/2010)
 */
Ext.ux.tree.TreeGridEditorNodeReader = function(tree) {
    this.tree = tree;
    this.root = tree.getRootNode();
};

Ext.ux.tree.TreeGridEditorNodeReader.prototype = {
    load: function(data) {
        var o = this.read(data);
        if (Ext.isArray(o)) {
            for (var i = 0; i < o.length; i++) {
                this.root.appendChild(this.loadNode(o[i]));
            }
        } else {
            this.root.appendChild(this.loadNode(o));
        }
    },
    
    loadNode: function(o) {
        var n = new Ext.ux.tree.TreeGridEditorNode(o);
        n.ui = new Ext.ux.tree.TreeGridEditorNodeUI(n);
        if (o.children) {
            for (var i = 0; i < o.children.length; i++) {
                n.appendChild(this.loadNode(o.children[i]));
            }
        }
        return n;
    },
    
    read: Ext.emptyFn
};

Ext.ux.tree.TreeGridEditorNodeJsonReader = Ext.extend(Ext.ux.tree.TreeGridEditorNodeReader, {
    read: function(response) {
        if (response == '') {
            return {};
        }
        return Ext.decode(response);
    }
});

Ext.ux.tree.TreeGridEditorNodeXmlReader = Ext.extend(Ext.ux.tree.TreeGridEditorNodeReader, {
    read: function(data) {
        var xotree = new Ext.ux.util.XMLObjTree();
        var jn = xotree.parseXML(data);
        //var dumper = new Ext.ux.util.JKLDumper();
        //var json = dumper.dump(jn);
        
        var o = [];
        
        var fn = function(o) {
            var n = {
                id: o['-id']
            };
            if (o.children) {
                n.children = [];
                var ns = o.children.node;
                if (Ext.isArray(ns)) {
                    for (var i = 0; i < ns.length; i++) {
                        n.children[i] = fn(ns[i]);
                    }
                } else {
                    n.children[0] = fn(ns);
                }
            }
            Ext.applyIf(n, o);
            delete n['-id'];
            for (var p in n) {
                n[p] = (n[p] === 'true' || n[p] === 'false') ? (n[p] === 'true' ? true : false) : n[p];
            }
            return n;
        };
        
        if (jn.nodes) {
            var ns = jn.nodes.node;
            if (Ext.isArray(ns)) {
                for (var i = 0; i < ns.length; i++) {
                    o[i] = fn(ns[i]);
                }
            } else {
                o[0] = fn(ns);
            }
        }
        return o;
    }
});

Ext.ns('Ext.ux.util');

//构造方法
Ext.ux.util.XMLObjTree = function() {
    return this;
};

Ext.ux.util.XMLObjTree.prototype.xmlDecl = '<?xml version="1.0" encoding="UTF-8" ?>\n';
Ext.ux.util.XMLObjTree.prototype.attr_prefix = '-';

//解析Ext.ux.util.XML串
Ext.ux.util.XMLObjTree.prototype.parseXML = function(xml) {
    var root;
    if (window.DOMParser) {
        var xmldom = new DOMParser();
        var dom = xmldom.parseFromString(xml, "application/xml");
        if (!dom) return;
        root = dom.documentElement;
    } else if (window.ActiveXObject) {
        xmldom = new ActiveXObject('Microsoft.XMLDOM');
        xmldom.async = false;
        xmldom.loadXML(xml);
        root = xmldom.documentElement;
    }
    if (!root) return;
    return this.parseDOM(root);
};

//  method: parseDOM( documentroot )

Ext.ux.util.XMLObjTree.prototype.parseDOM = function(root) {
    if (!root) return;
    this.__force_array = {};
    if (this.force_array) {
        for (var i = 0; i < this.force_array.length; i++) {
            this.__force_array[this.force_array[i]] = 1;
        }
    }
    var json = this.parseElement(root); // parse root node
    if (this.__force_array[root.nodeName]) {
        json = [json];
    }
    if (root.nodeType != 11) { // DOCUMENT_FRAGMENT_NODE
        var tmp = {};
        tmp[root.nodeName] = json; // root nodeName
        json = tmp;
    }
    return json;
};

Ext.ux.util.XMLObjTree.prototype.parseElement = function(elem) {
    //  如果是注释
    if (elem.nodeType == 7) {
        return;
    }
    
    //  如果是文本节点
    if (elem.nodeType == 3 || elem.nodeType == 4) {
        var bool = elem.nodeValue.match(/[^\x00-\x20]/);
        if (bool == null) return; // ignore white spaces
        return elem.nodeValue;
    }
    var retval;
    var cnt = {};
    
    //  有属性
    if (elem.attributes && elem.attributes.length) {
        retval = {};
        for (var i = 0; i < elem.attributes.length; i++) {
            var key = elem.attributes[i].nodeName;
            if (typeof(key) != "string") continue;
            var val = elem.attributes[i].nodeValue;
            if (!val) continue;
            key = this.attr_prefix + key;
            if (typeof(cnt[key]) == "undefined") cnt[key] = 0;
            cnt[key]++;
            this.addNode(retval, key, cnt[key], val);
        }
    }
    
    //  解吸子节点
    if (elem.childNodes && elem.childNodes.length) {
        var textonly = true;
        if (retval) textonly = false; // some attributes exists
        for (var i = 0; i < elem.childNodes.length && textonly; i++) {
            var ntype = elem.childNodes[i].nodeType;
            if (ntype == 3 || ntype == 4) continue;
            textonly = false;
        }
        if (textonly) {
            if (!retval) retval = "";
            for (var i = 0; i < elem.childNodes.length; i++) {
                retval += elem.childNodes[i].nodeValue;
            }
        } else {
            if (!retval) retval = {};
            for (var i = 0; i < elem.childNodes.length; i++) {
                var key = elem.childNodes[i].nodeName;
                if (typeof(key) != "string") continue;
                var val = this.parseElement(elem.childNodes[i]);
                if (!val) continue;
                if (typeof(cnt[key]) == "undefined") cnt[key] = 0;
                cnt[key]++;
                this.addNode(retval, key, cnt[key], val);
            }
        }
    }
    return retval;
};

//  添加节点
Ext.ux.util.XMLObjTree.prototype.addNode = function(hash, key, cnts, val) {
    if (this.__force_array[key]) {
        if (cnts == 1) hash[key] = [];
        hash[key][hash[key].length] = val; // push
    } else if (cnts == 1) { // 1st sibling
        hash[key] = val;
    } else if (cnts == 2) { // 2nd sibling
        hash[key] = [hash[key], val];
    } else { // 3rd sibling and more
        hash[key][hash[key].length] = val;
    }
};

//  把JSON树解析成XML
Ext.ux.util.XMLObjTree.prototype.writeXML = function(tree) {
    var xml = this.hash_to_xml(null, tree);
    return this.xmlDecl + xml;
};

//  method: hash_to_xml( tagName, tree )
Ext.ux.util.XMLObjTree.prototype.hash_to_xml = function(name, tree) {
    var elem = [];
    var attr = [];
    for (var key in tree) {
        if (!tree.hasOwnProperty(key)) continue;
        var val = tree[key];
        if (key.charAt(0) != this.attr_prefix) {
            if (typeof(val) == "undefined" || val == null) {
                elem[elem.length] = "<" + key + " />";
            } else if (typeof(val) == "object" && val.constructor == Array) {
                elem[elem.length] = this.array_to_xml(key, val);
            } else if (typeof(val) == "object") {
                elem[elem.length] = this.hash_to_xml(key, val);
            } else {
                elem[elem.length] = this.scalar_to_xml(key, val);
            }
        } else {
            attr[attr.length] = " " + (key.substring(1)) + '="' + (this.xml_escape(val)) + '"';
        }
    }
    var jattr = attr.join("");
    var jelem = elem.join("");
    if (typeof(name) == "undefined" || name == null) {
        // no tag
    } else if (elem.length > 0) {
        if (jelem.match(/\n/)) {
            jelem = "<" + name + jattr + ">\n" + jelem + "</" + name + ">\n";
        } else {
            jelem = "<" + name + jattr + ">" + jelem + "</" + name + ">\n";
        }
    } else {
        jelem = "<" + name + jattr + " />\n";
    }
    return jelem;
};

// 把数组解释为XML
Ext.ux.util.XMLObjTree.prototype.array_to_xml = function(name, array) {
    var out = [];
    for (var i = 0; i < array.length; i++) {
        var val = array[i];
        if (typeof(val) == "undefined" || val == null) {
            out[out.length] = "<" + name + " />";
        } else if (typeof(val) == "object" && val.constructor == Array) {
            out[out.length] = this.array_to_xml(name, val);
        } else if (typeof(val) == "object") {
            out[out.length] = this.hash_to_xml(name, val);
        } else {
            out[out.length] = this.scalar_to_xml(name, val);
        }
    }
    return out.join("");
};

//  method: scalar_to_xml( tagName, text )
Ext.ux.util.XMLObjTree.prototype.scalar_to_xml = function(name, text) {
    if (name == "#text") {
        return this.xml_escape(text);
    } else {
        return "<" + name + ">" + this.xml_escape(text) + "</" + name + ">\n";
    }
};

//  method: xml_escape( text )
Ext.ux.util.XMLObjTree.prototype.xml_escape = function(text) {
    return text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
};

Ext.ux.util.JKLDumper = function() {
    return this;
};

Ext.ux.util.JKLDumper.prototype.dump = function(data, offset) {
    if (typeof(offset) == "undefined") offset = "";
    var nextoff = offset + "  ";
    switch (typeof(data)) {
        case "string":
            return '"' + this.escapeString(data) + '"';
            break;
        case "number":
            return data;
            break;
        case "boolean":
            return data ? "true" : "false";
            break;
        case "undefined":
            return "null";
            break;
        case "object":
            if (data == null) {
                return "null";
            } else if (data.constructor == Array) {
                var array = [];
                for (var i = 0; i < data.length; i++) {
                    array[i] = this.dump(data[i], nextoff);
                }
                return "[\n" + nextoff + array.join(",\n" + nextoff) + "\n" + offset + "]";
            } else {
                var array = [];
                for (var key in data) {
                    var val = this.dump(data[key], nextoff);
                    key = '"' + this.escapeString(key) + '"';
                    array[array.length] = key + ": " + val;
                }
                if (array.length == 1 && !array[0].match(/[\n\{\[]/)) {
                    return "{ " + array[0] + " }";
                }
                return "{\n" + nextoff + array.join(",\n" + nextoff) + "\n" + offset + "}";
            }
            break;
        default:
            return data;
            // unsupported data type
            break;
    }
};

//  escape '\' and '"'
Ext.ux.util.JKLDumper.prototype.escapeString = function(str) {
    return str.replace(/\\/g, "\\\\").replace(/\"/g, "\\\"");
};
