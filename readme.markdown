

Usage
-----

Just run it from within the directory containing the images (*\*.png*) and the metrics data (*\*.xml*) and it will automatically produce the *\*.alf* and *\*.ali* files.

    java -jar AlbiteFontBuilder-0.9.1

**Notes:**

1. For the images containing the glyphs apply the same requirements as the ones mentioned in *AlbiteImageConverter*'s reademe. See it's readme for more info.
2. The xml file has some specific structure, too. It's almost identical to the one, produced by *Angel Code*'s [Bitmap font generator](http://www.angelcode.com/products/bmfont/). See next section for instructions

The *\*.xml* metrics file
-------------------------

Here goes an example of how should a file of this king look like:

    <font lineSpacing="0" lineHeight="18">
        <char id="32" x="12" y="26" width="1" height="1" xoffset="0" yoffset="14" xadvance="4" />
        <char id="35" x="9" y="40" width="8" height="11" xoffset="0" yoffset="3" xadvance="8" />
        <char id="37" x="0" y="16" width="12" height="12" xoffset="0" yoffset="3" xadvance="13" />
        <char id="40" x="16" y="0" width="4" height="14" xoffset="0" yoffset="2" xadvance="5" />
        <char id="41" x="20" y="0" width="4" height="14" xoffset="0" yoffset="2" xadvance="5" />
        <char id="45" x="8" y="14" width="4" height="2" xoffset="0" yoffset="9" xadvance="4" />
        <char id="46" x="30" y="26" width="2" height="3" xoffset="1" yoffset="12" xadvance="4" />
        <char id="47" x="1" y="0" width="5" height="14" xoffset="0" yoffset="2" xadvance="4" />
        <char id="48" x="12" y="14" width="7" height="12" xoffset="0" yoffset="3" xadvance="8" />
        <char id="49" x="17" y="38" width="7" height="11" xoffset="1" yoffset="3" xadvance="8" />
        <char id="50" x="24" y="38" width="7" height="11" xoffset="0" yoffset="3" xadvance="8" />
        <char id="51" x="19" y="14" width="7" height="12" xoffset="0" yoffset="3" xadvance="8" />
        <char id="52" x="0" y="40" width="9" height="11" xoffset="0" yoffset="3" xadvance="8" />
        <char id="53" x="0" y="28" width="7" height="12" xoffset="0" yoffset="3" xadvance="8" />
        <char id="54" x="7" y="28" width="7" height="12" xoffset="1" yoffset="3" xadvance="8" />
        <char id="55" x="0" y="51" width="7" height="11" xoffset="1" yoffset="3" xadvance="8" />
        <char id="56" x="14" y="26" width="7" height="12" xoffset="0" yoffset="3" xadvance="8" />
        <char id="57" x="21" y="26" width="7" height="12" xoffset="0" yoffset="3" xadvance="8" />
        <char id="58" x="28" y="26" width="2" height="10" xoffset="1" yoffset="5" xadvance="4" />
        <char id="60" x="7" y="51" width="7" height="8" xoffset="1" yoffset="5" xadvance="8" />
        <char id="62" x="14" y="51" width="7" height="8" xoffset="1" yoffset="5" xadvance="8" />
        <char id="63" x="26" y="14" width="6" height="12" xoffset="0" yoffset="3" xadvance="7" />
        <char id="91" x="24" y="0" width="4" height="14" xoffset="1" yoffset="2" xadvance="5" />
        <char id="93" x="28" y="0" width="4" height="14" xoffset="0" yoffset="2" xadvance="5" />
        <char id="95" x="1" y="14" width="7" height="2" xoffset="0" yoffset="15" xadvance="7" />
        <char id="123" x="6" y="0" width="5" height="14" xoffset="0" yoffset="2" xadvance="6" />
        <char id="124" x="0" y="0" width="1" height="16" xoffset="3" yoffset="2" xadvance="8" />
        <char id="125" x="11" y="0" width="5" height="14" xoffset="0" yoffset="2" xadvance="6" />
        <char id="183" x="21" y="49" width="2" height="3" xoffset="1" yoffset="7" xadvance="4" />
    </font>

**Notes:**

1. The `lineSpacing` and `lineHeight` attributes of the `font` tag are obligatory.
2. All fonts must include these obligatory characters: 32 (space), 45 (dash) and 63 (question mark) or the building would fail.

The Binary Font Metrics Descriptor File (*\*.alf*)
-------------------------------------------------

**Main overview**

<table border="0" cellpadding="5">
  <thead>
    <tr>
      <th>Meaning</th>
      <th>Type</th>
      <th>Bytes</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Magic number</td>
      <td>Integer</td>
      <td>4</td>
    </tr>
    <tr>
      <td>Line spacing</td>
      <td>Byte</td>
      <td>1</td>
    </tr>
    <tr>
      <td>Line height</td>
      <td>Byte</td>
      <td>1</td>
    </tr>
    <tr>
      <td>Largest ID in the font</td>
      <td>Integer</td>
      <td>4</td>
    </tr>
    <tr>
      <td>Maximum character width</td>
      <td>Short</td>
      <td>2</td>
    </tr>
    <tr>
      <td>Number of characters</td>
      <td>Integer</td>
      <td>4</td>
    </tr>
    <tr>
      <td>Character metrics blocks</td>
      <td>Block of several values</td>
      <td>18 * number of characters</td>
    </tr>
    <tr>
      <td></td>
      <td></td>
      <td></td>
    </tr>
  </tbody>
</table>

**Character block**
<table border="0" cellpadding="5">
  <thead>
    <tr>
      <th>Meaning</th>
      <th>Type</th>
      <th>Bytes</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>ID</td>
      <td>Integer</td>
      <td>4</td>
    </tr>
    <tr>
      <td>x-coordinate</td>
      <td>Short</td>
      <td>2</td>
    </tr>
    <tr>
      <td>y-coordinate</td>
      <td>Short</td>
      <td>2</td>
    </tr>
    <tr>
      <td>width</td>
      <td>Short</td>
      <td>2</td>
    </tr>
    <tr>
      <td>height</td>
      <td>Short</td>
      <td>2</td>
    </tr>
    <tr>
      <td>x-offset</td>
      <td>Short</td>
      <td>2</td>
    </tr>
    <tr>
      <td>y-offset</td>
      <td>Short</td>
      <td>2</td>
    </tr>
    <tr>
      <td>advance on x</td>
      <td>Short</td>
      <td>2</td>
    </tr>
  </tbody>
</table>
