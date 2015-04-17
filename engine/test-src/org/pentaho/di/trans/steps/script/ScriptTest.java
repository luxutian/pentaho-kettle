/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.script;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.BlockingRowSet;
import org.pentaho.di.core.RowSet;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.trans.steps.mock.StepMockHelper;

public class ScriptTest {
  private StepMockHelper<ScriptMeta, ScriptData> helper;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    helper = new StepMockHelper<ScriptMeta, ScriptData>( "test-script", ScriptMeta.class, ScriptData.class );
    when( helper.logChannelInterfaceFactory.create( any(), any( LoggingObjectInterface.class ) ) ).thenReturn(
      helper.logChannelInterface );
    when( helper.trans.isRunning() ).thenReturn( true );
    when( helper.initStepMetaInterface.getJSScripts() ).thenReturn(
      new ScriptValuesScript[] { new ScriptValuesScript( ScriptValuesScript.NORMAL_SCRIPT, "", "var i = 0;" ) } );
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testOutputDoneIfInputEmpty() throws Exception {
    Script step = new Script( helper.stepMeta, helper.stepDataInterface, 1, helper.transMeta, helper.trans );
    step.init( helper.initStepMetaInterface, helper.initStepDataInterface );

    RowSet rs = helper.getMockInputRowSet( new Object[0][0] );
    List<RowSet> in = new ArrayList<RowSet>();
    in.add( rs );
    step.setInputRowSets( in );

    rs = new BlockingRowSet( 5 );
    List<RowSet> out = new ArrayList<RowSet>();
    out.add( rs );
    step.setOutputRowSets( out );

    step.processRow( helper.processRowsStepMetaInterface, helper.processRowsStepDataInterface );

    out = step.getOutputRowSets();
    rs = out.get( 0 );

    assertTrue( "Script step is supposed to done with output if there is no input", rs.isDone() );

    rs.getRow();
  }

}
