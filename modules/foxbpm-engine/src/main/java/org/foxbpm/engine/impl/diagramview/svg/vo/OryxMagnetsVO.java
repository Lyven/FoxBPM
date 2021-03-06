/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author MAENLIANG
 */
package org.foxbpm.engine.impl.diagramview.svg.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.foxbpm.engine.impl.diagramview.vo.VONode;

/**
 * 
 * @author MAENLIANG
 * @date 2014-06-10
 * 
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class OryxMagnetsVO extends VONode {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6951721771706464408L;
	@XmlElement(name = "magnet", namespace = "http://www.b3mn.org/oryx")
	private List<OryxMagnetVO> oryxMagnetList;

	public List<OryxMagnetVO> getOryxMagnetList() {
		return oryxMagnetList;
	}

	public void setOryxMagnetList(List<OryxMagnetVO> oryxMagnetList) {
		this.oryxMagnetList = oryxMagnetList;
	}
}
