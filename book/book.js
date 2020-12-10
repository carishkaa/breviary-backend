const pkg = require("./package.json");

module.exports = {
  "title": "TASP Covid",
  "author": "Blindspot Solutions",
  "variables": {
    "version": pkg.version,
    "projectName": "TASP Covid"
  },
  "plugins": [
    "fontsettings",
    "katex",
    "uml",
  ],
  "root": "..",
  "pluginsConfig": {
    "fontsettings": {
      "theme": 'white', // 'sepia', 'night' or 'white',
      "family": 'sans', // 'serif' or 'sans',
      "size": 2         // 1 - 4
    },
    "uml": {
      "format": "png",
      "nailgun": false
    },
  },
  // Options for PDF generation
  "pdf": {
    // Add page numbers to the bottom of every page
    "pageNumbers": true,

    // Font size for the file  content
    "fontSize": 12,

    // Paper size for the pdf
    // Choices are [u’a0’, u’a1’, u’a2’, u’a3’, u’a4’, u’a5’, u’a6’, u’b0’, u’b1’, u’b2’, u’b3’, u’b4’, u’b5’, u’b6’, u’legal’, u’letter’]
    "paperSize": "a4",

    // Margin (in pts)
    // Note: 72 pts equals 1 inch
    "margin": {
      "right": 62,
      "left": 62,
      "top": 36,
      "bottom": 36
    },

    //Header HTML template. Available variables: _PAGENUM_, _TITLE_, _AUTHOR_ and _SECTION_.
    "headerTemplate": null,

    //Footer HTML template. Available variables: _PAGENUM_, _TITLE_, _AUTHOR_ and _SECTION_.
    "footerTemplate": null
  },
}
